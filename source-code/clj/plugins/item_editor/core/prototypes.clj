
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
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:route-template (string)(opt)}
  ;
  ; @return (map)
  ;  {:base-route (string)
  ;   :extended-route (string)}
  [extension-id item-namespace {:keys [route-template] :as editor-props}]
  (merge {}
         (if route-template {:base-route     (routes.helpers/base-route     extension-id item-namespace editor-props)
                             :extended-route (routes.helpers/extended-route extension-id item-namespace editor-props)})
         (param editor-props)))
