
(ns plugins.item-lister.views
    (:require [mid-fruits.candy           :refer [param return]]
              [plugins.item-lister.engine :as engine]
              [plugins.sortable.core      :refer [sortable]]
              [x.app-db.api               :as db]
              [x.app-components.api       :as components]
              [x.app-core.api             :as a :refer [r]]
              [x.app-elements.api         :as elements]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) extension-name
  ;
  ; @return (map)
  ;  {:downloaded-items (vector)
  ;   :synchronizing? (boolean)}
  [db [_ extension-name]]
  {:downloaded-items (r engine/get-downloaded-items db extension-name)
   :synchronizing?   (r engine/synchronizing?       db extension-name)})

(a/reg-sub ::get-view-props get-view-props)



;; -- Search components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-search-mode-button
  ; @param (string) extension-name
  ;
  ; @usage
  ;  [item-lister/quit-search-mode-button "products"]
  ;
  ; @return (component)
  [extension-name]
  [elements/button ::quit-search-mode-button
                   {:on-click [:plugins/toggle-item-lister-search-mode! extension-name]
                    :preset   :close-icon-button}])

(defn search-mode-button
  ; @param (string) extension-name
  ;
  ; @usage
  ;  [item-lister/search-mode-button "products"]
  ;
  ; @return (component)
  [extension-name]
  [elements/button ::search-button
                   {:on-click [:plugins/toggle-item-lister-search-mode! extension-name]
                    :preset   :search-icon-button
                    :tooltip  :search}])

(defn search-items-field
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [item-lister/search-items-field "products" "product"]
  ;
  ; @return (component)
  [extension-name item-name]
  (let [extension-id (keyword extension-name)]
       [elements/search-field ::search-field
                              {:auto-focus?   true
                               :layout        :row
                               :min-width     :xs
                               :placeholder   :search
                               :on-type-ended [:item-lister/search-items! extension-name item-name]
                               :value-path    [extension-id :lister-meta :search-term]}]))

(defn search-header
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [item-lister/search-header "products" "product"]
  ;
  ; @return (component)
  [extension-name item-name]
  [:div.x-search-bar-a [:div.x-search-bar-a--search-field
                         [search-items-field extension-name item-name]]
                       [quit-search-mode-button extension-name]])



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item-button
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [item-lister/new-item-button "products" "product"]
  ;
  ; @return (component)
  [extension-name item-name]
  [elements/button ::new-item-button
                   ;:on-click [:x.app-router/go-to! "/products/new-product"]
                   {:on-click [:x.app-router/go-to! (str "/" extension-name "/new-" item-name)]
                    :preset   :add-icon-button
                    :tooltip  :add-new!}])

(defn new-item-select
  ; @param (string) extension-name
  ; @param (map) element-props
  ;  {:options (vector)}
  ;
  ; @usage
  ;  [item-lister/new-item-select "media" {:options [:create-directory! :upload-files!]}]
  ;
  ; @return (component)
  [extension-name {:keys [options]}]
  [elements/select ::new-item-select
                   {:as-button?      true
                    :autoclear?      true
                    :initial-options options
                   ;:on-select       [:media/add-new-item!]
                    :on-select       [(keyword extension-name "add-new-item!")]
                    :preset          :add-icon-button}])

(defn- select-multiple-items-button
  ; @param (string) extension-name
  ;
  ; @usage
  ;  [item-lister/select-multiple-items-button "products"]
  ;
  ; @return (component)
  [extension-name]
  [elements/button ::select-multiple-items-button
                   {:on-click [:item-lister/toggle-select-mode! extension-name]
                    :preset  :select-more-icon-button
                    :tooltip :check}])

(defn- delete-selected-items-button
  ; @param (string) extension-name
  ;
  ; @usage
  ;  [item-lister/delete-selected-items-button "products"]
  ;
  ; @return (component)
  [extension-name]
  [elements/button ::delete-selected-items-button
                   {:on-click [:item-lister/delete-selected-items! extension-name]
                    :preset   :delete-icon-button
                    :tooltip  :delete!}])

(defn- sort-items-button
  ; @param (string) extension-name
  ; @param (string) item-name
  ; @param (map) element-props
  ;  {:initial-value (keyword)
  ;   :options (vector)}
  ;
  ; @usage
  ;  [item-lister/sort-items-button "products" {:options [:by-name :by-date]
  ;                                             :initial-value :by-name}]
  ;
  ; @return (component)
  [extension-name item-name {:keys [initial-value options]}]
  (let [extension-id (keyword extension-name)]
       [elements/select ::sort-items-button
                        {:as-button?      true
                         :on-select       [:item-lister/order-items! extension-name item-name]
                         :options-label   :order-by
                         :preset          :order-by-icon-button
                         :tooltip         :order-by
                         :initial-value   initial-value
                         :initial-options options
                         :value-path      [extension-id :lister-meta :order-by]}]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request-indicator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) extension-name
  ; @param (string) item-name
  ; @param (map) lister-props
  ; @param (map) view-props
  ;  {:synchronizing? (boolean)}
  ;
  ; @return (component)
  [_ _ _ {:keys [synchronizing?]}]
  (if (boolean synchronizing?)
      [:div.item-lister--items--request-indicator [components/content {:content :downloading-items...}]]))

(defn- no-items-to-show-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) extension-name
  ; @param (string) item-name
  ; @param (map) lister-props
  ; @param (map) view-props
  ;  {:synchronizing? (boolean)}
  ;
  ; @return (component)
  [_ _ _ {:keys [synchronizing?]}]
  (if (not synchronizing?)
      [:div.item-lister--items--no-items-to-show [components/content {:content :no-items-to-show}]]))

(defn- sortable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) extension-name
  ; @param (string) item-name
  ; @param (map) lister-props
  ; @param (map) view-props
  ;
  ; @return (component)
  [extension-name item-name lister-props view-props]
  [sortable {}])

(defn- non-sortable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) extension-name
  ; @param (string) item-name
  ; @param (map) lister-props
  ;  {:list-element (component)
  ;   :common-props (map)(opt)}
  ; @param (map) view-props
  ;  {:downloaded-items (vector)}
  ;
  ; @return (hiccup)
  [extension-name item-name {:keys [list-element] :as lister-props}
                            {:keys [downloaded-items]          :as   view-props}]
  [:div.item-lister--item-list
    (map-indexed (fn [item-dex item]
                    ^{:key (db/document->document-id item)}
                     [list-element item-dex item])
                 (param downloaded-items))])

(defn- item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) extension-name
  ; @param (string) item-name
  ; @param (map) lister-props
  ; @param (map) view-props
  ;
  ; @return (component)
  [extension-name item-name {:keys [sortable?] :as lister-props} view-props]
  (if (boolean sortable?)
      [sortable-item-list     extension-name item-name lister-props view-props]
      [non-sortable-item-list extension-name item-name lister-props view-props]))

(defn- item-lister
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) extension-name
  ; @param (string) item-name
  ; @param (map) lister-props
  ;  {:sortable? (boolean)(opt)}
  ; @param (map) view-props
  ;  {:downloaded-items (vector)}
  ;
  ; @return (hiccup)
  [extension-name item-name {:keys [sortable?]        :as lister-props}
                            {:keys [downloaded-items] :as   view-props}]
  (let [extension-id (keyword extension-name)]
       [:div.item-lister--list-items
         [item-list                  extension-name item-name lister-props view-props]
         [components/infinite-loader extension-id {:on-viewport [:item-lister/request-items! extension-name item-name]}]
         [request-indicator          extension-name item-name lister-props view-props]
         ; "No items to show" label
         (if (empty? downloaded-items)
             [no-items-to-show-label extension-name item-name lister-props view-props])]))

(defn view
  ; Az item-lister paraméterként nem kaphatja meg a listaelemek számára átadandó közös
  ; paraméter térképet (common-props).
  ; Ha az item-lister paraméterként kapná meg a common-props értékét, akkor a common-props
  ; megváltozása az item-lister újrarendelődésével járna, ami az infinite-loader
  ; komponens újratöltését okozná.
  ;
  ; Pl.: clients modul, client-list nézet
  ;      Megváltozik a kiválasztott nyelv -> újrarenderelődne a lista
  ;
  ; @param (string) extension-name
  ; @param (string) item-name
  ; @param (map) lister-props
  ;  {:list-element (component)
  ;   :item-groups (keywords in vector)(opt)}
  ;
  ; @usage
  ;  [item-lister "clients" "client" {...}]
  ;
  ; @usage
  ;  (defn my-list-element [item-dex item] [:div ...])
  ;  [item-lister "clients" "client" {:element #'my-list-element}]
  ;
  ; @usage
  ;  (defn my-list-element [item-dex item ] [:div ...])
  ;  [item-lister "foods" "food" {:element     #'my-list-element
  ;                               :item-groups [:fruit :vegetable]}]
  ;
  ; @return (component)
  [extension-name item-name lister-props]
  (let [view-props (a/subscribe [::get-view-props extension-name])]
       (fn [] [item-lister extension-name item-name lister-props @view-props])))
