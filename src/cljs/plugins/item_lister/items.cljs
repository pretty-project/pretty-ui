
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
  {:items          (r engine/get-items             db lister-id)
   :synchronizing? (r engine/listening-to-request? db lister-id)})

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-indicator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id lister-props {:keys [synchronizing?]}]
  (if (boolean synchronizing?)
      [:div.item-lister--items--request-indicator [components/content {:content :downloading-items...}]]))

(defn no-items-to-show-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ {:keys [synchronizing?]}]
  (if (not synchronizing?)
      [:div.item-lister--items--no-items-to-show [components/content {:content :no-items-to-show}]]))

(defn sortable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id lister-props view-props]
  [sortable {}])

(defn non-sortable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id {:keys [element common-props] :as lister-props}
             {:keys [items]                :as view-props}]
 [:div.item-lister--item-list
   (map-indexed (fn [item-dex item]
                   ^{:key (db/document->document-id item)}
                    [element lister-id item-dex item common-props])
                (param items))])

(defn- item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id {:keys [sortable?] :as lister-props} view-props]
  (if (boolean sortable?)
      [sortable-item-list     lister-id lister-props view-props]
      [non-sortable-item-list lister-id lister-props view-props]))

(defn- subscriber-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A subscriber használatával elkerülhető, hogy az item-lister paraméterként kapja meg
  ; a listaelemek számára átadandó közös paraméter térképet (common-props).
  ; Ha az item-lister paraméterként kapná meg a common-props értékét, akkor a common-props
  ; megváltozása az item-lister újrarendelődésével járna, ami az infinite-loader
  ; komponens újratöltését okozná.
  [lister-id {:keys [subscriber] :as lister-props} view-props]
  (let [common-props (a/subscribe subscriber)]
       (fn [_ _ view-props] (let [lister-props (assoc lister-props :common-props @common-props)]
                                 [item-list lister-id lister-props view-props]))))

(defn items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id {:keys [on-list-ended request-id sortable? subscriber] :as lister-props}
             {:keys [items]                                         :as view-props}]
  [:div.item-lister--items
    (if (some? subscriber)
        [subscriber-item-list lister-id lister-props view-props]
        [item-list            lister-id lister-props view-props])
    (if (some? on-list-ended)
        [components/infinite-loader lister-id {:on-viewport on-list-ended}])
    (if (some? request-id)
        [request-indicator lister-id lister-props view-props])
    (if (empty? items)
        [no-items-to-show-label lister-id lister-props view-props])])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id lister-props]
  (let [view-props (a/subscribe [::get-view-props lister-id])]
       (fn [] [items lister-id lister-props @view-props])))
