
(ns pretty-elements.crumb-group.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]
              [pretty-models.api :as pretty-models]))

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
            (pretty-standards/standard-flex-props)
            (pretty-standards/standard-inner-position-props)
            (pretty-standards/standard-inner-size-props)
            (pretty-standards/standard-outer-position-props)
            (pretty-standards/standard-outer-size-props)
            (pretty-rules/auto-align-scrollable-flex)
            (pretty-rules/auto-set-mounted)
            (pretty-subitems/subitem-group<-subitem-default :crumbs)
            (pretty-subitems/subitem-group<-disabled-state  :crumbs)
            (pretty-subitems/leave-disabled-state           :crumbs)
            (pretty-subitems/apply-group-item-prototype     :crumbs crumb-prototype)))
