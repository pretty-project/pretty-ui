
(ns pretty-layouts.surface.prototypes
    (:require [pretty-models.api     :as pretty-models]
              [pretty-properties.api :as pretty-properties]
              [pretty-subitems.api   :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:footer-overlapping? (boolean)(opt)
  ;  ...}
  ; @param (map) footer
  ;
  ; @return (map)
  [id {:keys [footer-overlapping?]} footer]
  (if footer-overlapping? (-> footer (pretty-properties/default-border-props {:border-color :default     :border-position :top :border-width :xxs}))
                          (-> footer (pretty-properties/default-border-props {:border-color :transparent :border-position :top :border-width :xxs}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:header-overlapping? (boolean)(opt)
  ;  ...}
  ; @param (map) header
  ;
  ; @return (map)
  [id {:keys [header-overlapping?]} header]
  (if header-overlapping? (-> header (pretty-properties/default-border-props {:border-color :default     :border-position :bottom :border-width :xxs}))
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
                 (pretty-models/flex-container-standard-props)
                 (pretty-models/flex-container-rules)
                 (pretty-models/plain-content-standard-props)
                 (pretty-models/plain-content-rules)
                 (pretty-subitems/subitems<-disabled-state :body :footer :header)
                 (pretty-subitems/apply-subitem-prototype  :footer footer-prototype-f)
                 (pretty-subitems/apply-subitem-prototype  :header header-prototype-f))))
