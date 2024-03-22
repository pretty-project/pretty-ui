
(ns pretty-elements.menu-bar.prototypes
    (:require [pretty-models.api     :as pretty-models]
              [pretty-properties.api :as pretty-properties]
              [pretty-subitems.api   :as pretty-subitems]))

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
  (-> props (pretty-properties/default-flex-props       {:orientation :horizontal :overflow :scroll})
            (pretty-properties/default-outer-size-props {:outer-height :content :outer-size-unit :double-block :outer-width :content})
            (pretty-models/flex-container-standard-props)
            (pretty-models/flex-container-rules)
            (pretty-subitems/subitem-group<-subitem-default :menu-items)
            (pretty-subitems/subitem-group<-disabled-state  :menu-items)))
