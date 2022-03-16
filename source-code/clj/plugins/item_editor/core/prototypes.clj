
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
  ; @param (map) editor-props
  ;
  ; @return (map)
  ;  {:base-route (string)
  ;   :extended-route (string)
  ;   :route-title (metamorphic-content)}
  [extension-id item-namespace editor-props]
  (merge {:base-route     (routes.helpers/base-route     extension-id item-namespace editor-props)
          :extended-route (routes.helpers/extended-route extension-id item-namespace editor-props)
          :route-title    (param extension-id)}
         (param editor-props)))
