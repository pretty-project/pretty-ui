
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.prototypes
    (:require [mid-fruits.candy                   :refer [param]]
              [plugins.item-editor.routes.helpers :as routes.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:route-template (string)(opt)}
  ;
  ; @return (map)
  ;  {:base-route (string)
  ;   :extended-route (string)}
  [editor-id {:keys [route-template] :as editor-props}]
  (merge {}
         (if route-template {:base-route     (routes.helpers/base-route     editor-id editor-props)
                             :extended-route (routes.helpers/extended-route editor-id editor-props)})
         (param editor-props)))
