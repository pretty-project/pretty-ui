
(ns pretty-elements.image.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [react-references.api :as react-references]
              [dynamic-props.api :as dynamic-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sensor-props-prototype
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ; {:background-uri (string)(opt)}
  ;
  ; @return (map)
  ; {:on-load-f (function)
  ;  :uri (string)}
  [image-id {:keys [background-uri]}]
  (let [on-load-f (fn [_] (dynamic-props/merge-props! image-id {:loaded? true}))]
       {:on-load-f on-load-f
        :uri       background-uri}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-props-prototype
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ;
  ; @return (map)
  [image-id image-props]
  (let [set-reference-f (react-references/set-reference-f image-id)]
       (-> image-props (pretty-properties/clickable-text-auto-props)
                       (pretty-properties/default-anchor-props           {})
                       (pretty-properties/default-animation-props        {:animation-duration 2000 :animation-mode :repeat :animation-name :pulse})
                       (pretty-properties/default-background-color-props {:fill-color :highlight})
                       (pretty-properties/default-background-image-props {:background-size :contain})
                       (pretty-properties/default-border-props           {})
                       (pretty-properties/default-canvas-props           {:canvas-height :grow :canvas-width :parent})
                       (pretty-properties/default-effect-props           {})
                       (pretty-properties/default-flex-props             {:orientation :vertical})
                       (pretty-properties/default-font-props             {:font-size :xs :font-weight :medium})
                       (pretty-properties/default-icon-props             {:icon :image :icon-color :muted})
                       (pretty-properties/default-label-props            {})
                       (pretty-properties/default-mouse-event-props      {})
                       (pretty-properties/default-react-props            {:set-reference-f set-reference-f})
                       (pretty-properties/default-size-props             {:height :s :width :s})
                       (pretty-properties/default-text-props             {:text-overflow :ellipsis :text-selectable? false}))))
