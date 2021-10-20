(ns plugins.item-lister.items
  (:require [x.app-core.api :as a]
            [x.app-components.api :as components]))


(defn get-view-props [db _]
  {:sort-by ""})

(defn item-list [lister-id lister-props  items]
  [:div
   (map-indexed
     (fn [index item]))
   (str items)])

(defn view [lister-id {:keys [download-items-event value-path]
                       :as   lister-props}]
  [:<>
     [item-list lister-id lister-props @(a/subscribe [:x.app-db/get-item value-path])]
     [components/infinite-loader
        {:on-viewport download-items-event}]])