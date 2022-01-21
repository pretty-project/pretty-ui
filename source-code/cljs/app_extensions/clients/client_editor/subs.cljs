
(ns app-extensions.clients.client-editor.subs
    (:require [mid-fruits.candy      :refer [param]]
              [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]
              [x.app-locales.api     :as locales]
              [app-plugins.item-editor.api :as item-editor]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-client-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [first-name (get-in db [:clients :item-editor/data-items :first-name])
        last-name  (get-in db [:clients :item-editor/data-items :last-name])]
       (r locales/get-ordered-name db first-name last-name)))

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (merge (r item-editor/get-body-props db :clients :client)
         {:item-name         (r get-client-name               db)
          :name-order        (r locales/get-name-order        db)
          :selected-language (r locales/get-selected-language db)
          :viewport-large?   (r environment/viewport-large?   db)}))

(a/reg-sub :client-editor/get-body-props get-body-props)
