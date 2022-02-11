
(ns app-extensions.clients.client-editor.subs
    (:require [x.app-core.api    :as a :refer [r]]
              [x.app-locales.api :as locales]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-client-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [first-name (get-in db [:clients :item-editor/data-items :first-name])
        last-name  (get-in db [:clients :item-editor/data-items :last-name])]
       (r locales/get-ordered-name db first-name last-name)))

(a/reg-sub :clients.client-editor/get-client-name get-client-name)
