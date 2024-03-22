
(ns pretty-inputs.option-group.prototypes
    (:require [fruits.vector.api     :as vector]
              [pretty-models.api     :as pretty-models]
              [pretty-properties.api :as pretty-properties]
              [pretty-subitems.api   :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn option-prototype
  ; @ignore
  ;
  ; @param (integer) dex
  ; @param (map) option
  ;
  ; @return (map)
  [_ option]
  (-> option (pretty-properties/default-outer-size-props {:outer-width :parent})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn placeholder-text-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:options (*)(opt)
  ;  ...}
  ; @param (map) placeholder-text
  ;
  ; @return (map)
  [_ {:keys [options] :as props} placeholder-text]
  (if-not (-> options vector/not-empty?)
          (-> placeholder-text)))

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
  (let [placeholder-text-prototype-f (fn [%] (placeholder-text-prototype id props %))]
       (-> props (pretty-properties/default-flex-props       {:orientation :vertical})
                 (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                 (pretty-models/flex-container-standard-props)
                 (pretty-models/flex-container-rules)
                 (pretty-subitems/subitem-group<-subitem-default :options)
                 (pretty-subitems/subitem-group<-disabled-state  :options)
                 (pretty-subitems/apply-group-item-prototype     :options option-prototype)
                 (pretty-subitems/apply-subitem-prototype        :placeholder-text placeholder-text-prototype-f))))
