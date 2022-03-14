
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.target-handler.helpers
    (:require [mid-fruits.candy      :refer [param]]
              [x.app-core.api        :as a]
              [x.app-environment.api :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-id->target-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Egyes esetekben az elem DOM struktúrája nem teszi lehetővé, hogy az element-id
  ; az elem működését biztosító DOM elemet azonosítsa (pl.: input, button, ...)
  ;
  ; @param (keyword) element-id
  ;
  ; @example
  ;  (target-handler.helpers/element-id->target-id :my-namespace/my-element)
  ;  =>
  ;  "my-namespace--my-element--target"
  ;
  ; @return (string)
  [element-id]
  (a/dom-value element-id "target"))

(defn element-id->target-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [element-id]
  (-> element-id element-id->target-id environment/element-disabled?))

(defn element-id->target-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [element-id]
  (-> element-id element-id->target-id environment/element-enabled?))
