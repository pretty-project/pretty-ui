
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.3.6
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.views
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-db.api         :as db]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-tools.api      :as tools]
              [app-plugins.item-lister.engine :as engine]
              [app-plugins.sortable.core      :refer [sortable]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  ;  {:downloaded-items (vector)
  ;   :synchronizing? (boolean)}
  [db [_ extension-id]]
  {:downloaded-items (r engine/get-downloaded-items db extension-id)
   :synchronizing?   (r engine/synchronizing?       db extension-id)})

(a/reg-sub ::get-view-props get-view-props)



;; -- Search components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-search-mode-button
  ; @param (keyw) extension-id
  ;
  ; @usage
  ;  [item-lister/quit-search-mode-button :my-extension]
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::quit-search-mode-button
                   {:on-click [:item-lister/toggle-search-mode! extension-id]
                    :preset   :close-icon-button}])

(defn search-mode-button
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  [item-lister/search-mode-button :my-extension]
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::search-button
                   {:on-click [:item-lister/toggle-search-mode! extension-id]
                    :preset   :search-icon-button
                    :tooltip  :search}])

(defn search-items-field
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-lister/search-items-field :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  [elements/search-field ::search-items-field
                         {:auto-focus?   true
                          :layout        :row
                          :min-width     :xs
                          :on-empty      [:item-lister/search-items! extension-id item-namespace]
                          :on-type-ended [:item-lister/search-items! extension-id item-namespace]
                          :placeholder   :search
                          :value-path    [extension-id :lister-meta :search-term]}])

(defn search-header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-lister/search-header :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  [:div.x-search-bar-a [:div.x-search-bar-a--search-field
                         [search-items-field extension-id item-namespace]]
                       [quit-search-mode-button extension-id]])



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-lister/new-item-button :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [new-item-uri (engine/new-item-uri extension-id item-namespace)]
       [elements/button ::new-item-button
                        {:on-click [:x.app-router/go-to! new-item-uri]
                         :preset   :add-icon-button
                         :tooltip  :add-new!}]))

(defn new-item-select
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:options (vector)}
  ;
  ; @usage
  ;  (a/reg-event-fx :my-extension/add-new-item! (fn [_ [_ selected-option]] ...))
  ;  [item-lister/new-item-select :my-extension :my-type {:options [...]}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [options]}]
  [elements/select ::new-item-select
                   {:as-button?      true
                    :autoclear?      true
                    :initial-options (param options)
                    :on-select       (engine/add-new-item-event extension-id item-namespace)
                    :preset          :add-icon-button}])

(defn select-multiple-items-button
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  [item-lister/select-multiple-items-button :my-extension]
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::select-multiple-items-button
                   {:on-click [:item-lister/toggle-select-mode! extension-id]
                    :preset  :select-more-icon-button
                    :tooltip :check}])

(defn delete-selected-items-button
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  [item-lister/delete-selected-items-button :my-extension]
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::delete-selected-items-button
                   {:on-click [:item-lister/delete-selected-items! extension-id]
                    :preset   :delete-icon-button
                    :tooltip  :delete!}])

(defn toggle-item-filter-visibility-button
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  [item-lister/toggle-item-filter-visibility-button :my-extension]
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::toggle-item-filter-visibility-button
                   {:on-click [:item-lister/toggle-item-filter-visibility! extension-id]
                    :preset   :filters-icon-button
                    :tooltip  :filters}])

(defn sort-items-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:initial-value (keyword)
  ;   :options (vector)}
  ;
  ; @usage
  ;  [item-lister/sort-items-button :my-extension :my-type {:options      [:by-name :by-date]
  ;                                                         :initial-value :by-name}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [initial-value options]}]
  [elements/select ::sort-items-button
                   {:as-button?      true
                    :on-select       [:item-lister/order-items! extension-id item-namespace]
                    :options-label   :order-by
                    :preset          :order-by-icon-button
                    :tooltip         :order-by
                    :initial-value   initial-value
                    :initial-options options
                    :value-path      [extension-id :lister-meta :order-by]}])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request-indicator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
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
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
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
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ; @param (map) view-props
  ;
  ; @return (component)
  [extension-id item-namespace lister-props view-props]
  [sortable {}])

(defn- non-sortable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;  {:list-element (component)
  ;   :common-props (map)(opt)}
  ; @param (map) view-props
  ;  {:downloaded-items (vector)}
  ;
  ; @return (hiccup)
  [extension-id item-namespace {:keys [list-element]     :as lister-props}
                               {:keys [downloaded-items] :as   view-props}]
  [:div.item-lister--item-list
    (map-indexed (fn [item-dex item]
                    ^{:key (db/document->document-id item)}
                     [list-element item-dex item])
                 (param downloaded-items))])

(defn- item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ; @param (map) view-props
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [sortable?] :as lister-props} view-props]
  (if (boolean sortable?)
      [sortable-item-list     extension-id item-namespace lister-props view-props]
      [non-sortable-item-list extension-id item-namespace lister-props view-props]))

(defn- item-lister
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;  {:sortable? (boolean)(opt)}
  ; @param (map) view-props
  ;  {:downloaded-items (vector)}
  ;
  ; @return (hiccup)
  [extension-id item-namespace {:keys [sortable?]        :as lister-props}
                               {:keys [downloaded-items] :as   view-props}]
  [:div.item-lister--list-items
    [item-list             extension-id item-namespace lister-props view-props]
    [tools/infinite-loader extension-id {:on-viewport [:item-lister/request-items! extension-id item-namespace]}]
    [request-indicator     extension-id item-namespace lister-props view-props]
    ; "No items to show" label
    (if (empty? downloaded-items)
        [no-items-to-show-label extension-id item-namespace lister-props view-props])])

(defn view
  ; Az item-lister paraméterként nem kaphatja meg a listaelemek számára átadandó közös
  ; paraméter térképet (common-props).
  ; Ha az item-lister paraméterként kapná meg a common-props értékét, akkor a common-props
  ; megváltozása az item-lister újrarendelődésével járna, ami az infinite-loader
  ; komponens újratöltését okozná.
  ;
  ; Pl.: clients modul, client-list nézet
  ;      Megváltozik a kiválasztott nyelv -> újrarenderelődne a lista
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;  {:list-element (component)
  ;   :item-groups (keywords in vector)(opt)}
  ;
  ; @usage
  ;  [item-lister :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-list-element [item-dex item] [:div ...])
  ;  [item-lister :my-extension :my-type {:element #'my-list-element}]
  ;
  ; @usage
  ;  (defn my-list-element [item-dex item ] [:div ...])
  ;  [item-lister :my-extension :my-type {:element     #'my-list-element
  ;                                       :item-groups [:my-type :your-type]}]
  ;
  ; @return (component)
  [extension-id item-namespace lister-props]
  (let [view-props (a/subscribe [::get-view-props extension-id])]
       (fn [] [item-lister extension-id item-namespace lister-props @view-props])))
