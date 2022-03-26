
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.mount.events
    (:require [mid-fruits.map :refer [dissoc-in]]
              [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ selector-id body-props]]
  (assoc-in db [:plugins :view-selector/body-props selector-id] body-props))

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ;
  ; @return (map)
  [db [_ selector-id]]
  (-> db (dissoc-in [:plugins :view-selector/body-props selector-id])
         (dissoc-in [:plugins :view-selector/meta-items selector-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :view-selector/body-did-mount body-did-mount)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :view-selector/body-will-unmount body-will-unmount)
