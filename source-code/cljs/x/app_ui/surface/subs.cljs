
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface.subs
    (:require [x.app-core.api    :as a :refer [r]]
              [x.app-ui.renderer :as renderer]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-surface-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (keyword) prop-key
  ;
  ; @return (boolean)
  [db [_ surface-id prop-key]]
  (r renderer/get-element-prop db :surface surface-id prop-key))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :ui/get-surface-prop get-surface-prop)
