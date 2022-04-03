
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.subs
    (:require [x.app-core.api    :as a :refer [r]]
              [x.app-locales.api :as locales]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-client-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ item-dex]]
  (let [first-name (get-in db [:clients :client-lister/downloaded-items item-dex :first-name])
        last-name  (get-in db [:clients :client-lister/downloaded-items item-dex :last-name])]
       (r locales/get-ordered-name db first-name last-name)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :clients.client-lister/get-client-name get-client-name)
