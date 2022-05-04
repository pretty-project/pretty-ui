
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-editor.subs
    (:require [x.app-core.api    :as a :refer [r]]
              [x.app-locales.api :as locales]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-client-name
  [db _]
  (let [first-name (get-in db [:clients :client-editor/edited-item :first-name])
        last-name  (get-in db [:clients :client-editor/edited-item :last-name])]
       (r locales/get-ordered-name db first-name last-name)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-sub :clients.client-editor/get-client-name get-client-name)
