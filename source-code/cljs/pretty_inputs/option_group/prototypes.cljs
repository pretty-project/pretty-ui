
(ns pretty-inputs.option-group.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api      :as pretty-rules]
              [pretty-standards.api  :as pretty-standards]
              [pretty-subitems.api   :as pretty-subitems]
              [fruits.vector.api :as vector]))

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
       (-> props (pretty-properties/default-flex-props             {:orientation :vertical})
                 (pretty-properties/default-input-validation-props {:validate-when-change? true})
                 (pretty-properties/default-outer-size-props       {:outer-size-unit :full-block})
                 (pretty-standards/standard-flex-props)
                 (pretty-standards/standard-inner-position-props)
                 (pretty-standards/standard-inner-size-props)
                 (pretty-standards/standard-outer-position-props)
                 (pretty-standards/standard-input-option-props)
                 (pretty-standards/standard-outer-size-props)
                 (pretty-rules/auto-align-scrollable-flex)
                 (pretty-rules/auto-disable-mouse-events)
                 (pretty-rules/auto-set-mounted)
                 (pretty-subitems/subitem-group<-subitem-default :options)
                 (pretty-subitems/subitem-group<-disabled-state  :options)
                 (pretty-subitems/leave-disabled-state           :options)
                 (pretty-subitems/apply-group-item-prototype     :options option-prototype)
                 (pretty-subitems/apply-subitem-prototype        :placeholder-text placeholder-text-prototype-f))))
