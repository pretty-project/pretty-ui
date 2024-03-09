
(ns pretty-layouts.surface.prototypes
    (:require [pretty-layouts.engine.api :as pretty-layouts.engine]
              [pretty-properties.api     :as pretty-properties]
              [pretty-models.api :as pretty-models]
              [pretty-subitems.api       :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) footer
  ;
  ; @return (map)
  [id _ footer]
  (if (pretty-layouts.engine/layout-sensor-overlapping?  (pretty-subitems/subitem-id id :footer-sensor) footer)
      (-> footer (pretty-properties/default-border-props {:border-color :default     :border-position :top :border-width :xxs}))
      (-> footer (pretty-properties/default-border-props {:border-color :transparent :border-position :top :border-width :xxs}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) header
  ;
  ; @return (map)
  [id _ header]
  (if (pretty-layouts.engine/layout-sensor-overlapping?  (pretty-subitems/subitem-id id :header-sensor) header)
      (-> header (pretty-properties/default-border-props {:border-color :default     :border-position :bottom :border-width :xxs}))
      (-> header (pretty-properties/default-border-props {:border-color :transparent :border-position :bottom :border-width :xxs}))))

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
  (let [footer-prototype-f (fn [%] (footer-prototype id props %))
        header-prototype-f (fn [%] (header-prototype id props %))]
       (-> props (pretty-properties/default-flex-props           {:orientation :vertical})
                 (pretty-properties/default-outer-size-props     {:outer-height :parent :outer-size-unit :screen :outer-width :parent})
                 (pretty-properties/default-overflow-props       {:vertical-overflow :scroll})
                 (pretty-models/container-model-standard-props)
                 (pretty-models/container-model-rules)
                 (pretty-models/content-model-standard-props)
                 (pretty-models/content-model-rules)
                 (pretty-subitems/subitems<-disabled-state :body :footer :header)
                 (pretty-subitems/apply-subitem-prototype  :footer footer-prototype-f)
                 (pretty-subitems/apply-subitem-prototype  :header header-prototype-f))))
