package com.stukovegor.scs.Tools;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Наследник класса {@link OrthogonalTiledMapRenderer}, позволяющий
 * избежать вертикальных линий на карте во время движения камеры.
 * За основу взят класс GulaOrthogonalTiledMapRenderer,
 * взятый с форума badlogicgames.com/forum
 * @author j3nda, Стуков Егор
 */
public class AntiBleedingOrthogonalTiledMapRenderer extends OrthogonalTiledMapRenderer {

    /**
     * Конструктор
     * @param map - передаваемая карта
     * @param unitScale - масштабирование к какой либо константе
     * @param batch - передаваемый отрисовщик
     */
    public AntiBleedingOrthogonalTiledMapRenderer(TiledMap map, float unitScale, Batch batch) {
        super(map, unitScale, batch);
    }

    /**
     * Метод, "лечащий" карту
     * @param tiledMap - передаваемая карта
     * @return - "вылеченная карта"
     */
    private static TiledMap fixTilesPixelBleeding(TiledMap tiledMap) {
        for(MapLayer layer : tiledMap.getLayers())
        {
            if (!layer.isVisible())
            {
                continue;
            }
            if (layer instanceof TiledMapTileLayer)
            {
                fixTilePixelBleeding((TiledMapTileLayer) layer);
            }
        }
        return tiledMap;
    }

    /**
     * Метод, "лечащий" слой карты
     * @param layer - передаваемый слой
     */
    private static void fixTilePixelBleeding(TiledMapTileLayer layer) {
        for(int x = 0; x < layer.getWidth(); x++)
        {
            for(int y = 0; y < layer.getHeight(); y++)
            {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null)
                {
                    fixPixelBleeding(cell.getTile().getTextureRegion());
                }
            }
        }
    }

    /**
     * Метод, "лечащий" регион текстур
     * @param region - передаваемый регион текстур
     */
    private static void fixPixelBleeding(TextureRegion region) {

        float fix = 0.01f;

        float x = region.getRegionX();
        float y = region.getRegionY();
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float invTexWidth = 1f / region.getTexture().getWidth();
        float invTexHeight = 1f / region.getTexture().getHeight();

        region.setRegion(
                (x + fix) * invTexWidth,
                (y + fix) * invTexHeight,
                (x + width - fix) * invTexWidth,
                (y + height - fix) * invTexHeight
        );
    }

    /**
     * Устанавливаем карту, за одно и "лечим" её
     * @param map - передаваемая карта
     */
    @Override
    public void setMap(TiledMap map) {
        super.setMap(fixTilesPixelBleeding(map));
    }
}
