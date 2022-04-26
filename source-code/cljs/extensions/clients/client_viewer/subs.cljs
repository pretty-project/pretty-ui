
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-viewer.subs
    (:require [x.app-core.api    :as a :refer [r]]
              [x.app-locales.api :as locales]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-client-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [first-name (get-in db [:clients :client-viewer/viewed-item :first-name])
        last-name  (get-in db [:clients :client-viewer/viewed-item :last-name])]
       (r locales/get-ordered-name db first-name last-name)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :clients.client-viewer/get-client-name get-client-name)
