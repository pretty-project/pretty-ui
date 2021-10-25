
(ns plugins.item-lister.items
    (:require [mid-fruits.candy           :refer [param return]]
              [plugins.item-lister.engine :as engine]
              [plugins.sortable.core      :refer [sortable]]
              [x.app-db.api               :as db]
              [x.app-components.api       :as components]
              [x.app-core.api             :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {}
  [db [_ lister-id]]
  {:items                 (r engine/get-items             db lister-id)
   :listening-to-request? (r engine/listening-to-request? db lister-id)})

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-indicator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id lister-props {:keys [listening-to-request?]}]
  (if (boolean listening-to-request?)
      [:div.item-lister--items--request-indicator [components/content {:content :downloading-items...}]]))

(defn sortable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id lister-props view-props]
  [sortable {}])

(defn static-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id {:keys [element common-props] :as lister-props}
             {:keys [items]                :as view-props}]
 [:div.item-lister--item-list
   (map-indexed (fn [item-dex item]
                   ^{:key (db/document->document-id item)}
                    [element lister-id item-dex item common-props])
                (param items))])

(defn items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id {:keys [on-list-ended request-id sortable?] :as lister-props} view-props]
  [:div.item-lister--items
    (if (boolean sortable?)
        [sortable-item-list lister-id lister-props view-props]
        [static-item-list   lister-id lister-props view-props])
    (if (some? on-list-ended)
        [components/infinite-loader lister-id {:on-viewport on-list-ended}])
    (if (some? request-id)
        [request-indicator lister-id lister-props view-props])])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id lister-props]
  (let [view-props (a/subscribe [::get-view-props lister-id])]
       (fn [] [items lister-id lister-props @view-props])))
