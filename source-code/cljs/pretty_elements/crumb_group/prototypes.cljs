
(ns pretty-elements.crumb-group.prototypes
    (:require [pretty-models.api     :as pretty-models]
              [pretty-properties.api :as pretty-properties]
              [pretty-subitems.api   :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-prototype
  ; @ignore
  ;
  ; @param (integer) dex
  ; @param (map) crumb
  ;
  ; @return (map)
  [dex crumb]
  (if (-> dex zero?)
      (-> crumb (dissoc :bullet))
      (-> crumb)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [_ props]
  (-> props (pretty-properties/default-flex-props       {:gap :xs :orientation :horizontal :overflow :scroll})
            (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
            (pretty-models/flex-container-standard-props)
            (pretty-models/flex-container-rules)
            (pretty-subitems/subitem-group<-subitem-default :crumbs)
            (pretty-subitems/subitem-group<-disabled-state  :crumbs)
            (pretty-subitems/apply-group-item-prototype     :crumbs crumb-prototype)))
