
(ns pretty-elements.crumb-group.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-props-prototype
  ; @ignore
  ;
  ; @param (integer) crumb-dex
  ; @param (map) crumb-props
  ;
  ; @return (map)
  [crumb-dex crumb-props]
  (if (-> crumb-dex zero?)
      (-> crumb-props (dissoc :bullet))
      (-> crumb-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  [_ group-props]
  (-> group-props (pretty-properties/default-flex-props       {:gap :xs :orientation :horizontal :overflow :scroll})
                  (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                  (pretty-standards/standard-flex-props)
                  (pretty-standards/standard-inner-position-props)
                  (pretty-standards/standard-inner-size-props)
                  (pretty-standards/standard-outer-position-props)
                  (pretty-standards/standard-outer-size-props)
                  (pretty-rules/auto-align-scrollable-flex)
                  (pretty-subitems/subitem-group<-subitem-default :crumbs)
                  (pretty-subitems/subitem-group<-disabled-state  :crumbs)
                  (pretty-subitems/leave-disabled-state           :crumbs)
                  (pretty-subitems/apply-group-item-prototype     :crumbs crumb-props-prototype)))
