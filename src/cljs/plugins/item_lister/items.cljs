(ns plugins.item-lister.items
  (:require
   [mid-fruits.candy :refer [param return]]
   [plugins.item-lister.engine :as engine]
   [x.app-db.api   :as db]
   [x.app-components.api :as components]
   [x.app-core.api :as a :refer [r]]))

;; ----------------------------------------------------------------------------
;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-view-props [db [_ lister-id]]
  {:sort-by (r engine/get-sort-by db lister-id)
   :items   (r engine/get-items   db lister-id)})

(a/reg-sub ::get-view-props get-view-props)

;; ----------------------------------------------------------------------------
;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-list [lister-id {:keys [element] :as lister-props}
                           {:keys [sort-by items]}]
  [:div
   (map-indexed
     (fn [item-dex item]
       ^{:key (db/document->document-id item)}
       [element lister-id item-dex item])
     (param items))])


(defn view [lister-id {:keys [download-items-event]
                       :as   lister-props}]
  [:<>
     [item-list lister-id lister-props (a/subscribed [::get-view-props lister-id])]
     [components/infinite-loader {:on-viewport download-items-event}]])

;; ----------------------------------------------------------------------------
;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------