
(ns elements.icon.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [icon-id {:keys [style] :as icon-props}]
  (-> {:data-selectable false
       :style           style}
      (pretty-css/icon-attributes   icon-props)
      (pretty-css/indent-attributes icon-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  [_ icon-props]
  (-> {} (pretty-css/default-attributes icon-props)
         (pretty-css/outdent-attributes icon-props)))
