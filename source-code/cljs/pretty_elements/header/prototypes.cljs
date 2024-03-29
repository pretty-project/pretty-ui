
(ns pretty-elements.header.prototypes
    (:require [pretty-models.api     :as pretty-models]
              [pretty-properties.api :as pretty-properties]
              [pretty-subitems.api   :as pretty-subitems]))

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
  ; @bug (#9811)
  ; - The content can be an empty string before it gets its eventual value (e.g., from a subscription or a HTTP request, etc.).
  ;   An empty content placeholder and a delayed content can cause a short flickering due to inconsistent content height!
  ;   Therefore, the content placeholder must have at least a blank character as its content (e.g., "\u00A0").
  (-> label (pretty-properties/default-content-props {:content-placeholder "\u00A0"})))

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
  (let [label-prototype-f (fn [%] (label-prototype id props %))]
       (-> props (pretty-properties/default-flex-props       {:orientation :horizontal})
                 (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                 (pretty-models/flex-container-standard-props)
                 (pretty-models/flex-container-rules)
                 (pretty-subitems/subitems<-disabled-state :start-adornment-group :end-adornment-group)
                 (pretty-subitems/apply-subitem-prototype  :label label-prototype-f))))
