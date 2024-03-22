
(ns pretty-elements.icon-button.prototypes
    (:require [pretty-models.api     :as pretty-models]
              [pretty-properties.api :as pretty-properties]
              [pretty-subitems.api   :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) icon
  ;
  ; @return (map)
  [_ _ icon]
  (-> icon (pretty-properties/default-icon-props {:icon-size :m})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) label
  ;
  ; @return (map)
  [_ _ label]
  (-> label (pretty-properties/default-font-props {:font-size :micro :font-weight :medium})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [id props]
  (let [icon-prototype-f  (fn [%] (icon-prototype  id props %))
        label-prototype-f (fn [%] (label-prototype id props %))]
       (-> props (pretty-properties/default-flex-props       {:orientation :vertical})
                 (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                 (pretty-models/click-control-standard-props)
                 (pretty-models/click-control-rules)
                 (pretty-models/flex-container-standard-props)
                 (pretty-models/flex-container-rules)
                 (pretty-subitems/apply-subitem-prototype :icon  icon-prototype-f)
                 (pretty-subitems/apply-subitem-prototype :label label-prototype-f))))
