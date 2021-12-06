
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
    (:require [mid-fruits.candy      :refer [param return]]
              [x.app-db.api          :as db]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-tools.api       :as tools]
              [app-fruits.react-transition    :as react-transition]
              [app-plugins.item-lister.engine :as engine]
              [app-plugins.sortable.core      :refer [sortable]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  ;  {:search-mode? (boolean)
  ;   :select-mode? (boolean)
  ;   :viewport-small? (boolean)}
  [db [_ extension-id]]
  {:search-mode?    (get-in db [extension-id :lister-meta :search-mode?])
   :select-mode?    (get-in db [extension-id :lister-meta :select-mode?])
   :viewport-small? (r environment/viewport-small? db)})

(a/reg-sub ::get-header-props get-header-props)

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:downloaded-items (vector)
  ;   :synchronizing? (boolean)}
  [db [_ extension-id item-namespace]]
  {:downloaded-items (r engine/get-downloaded-items db extension-id item-namespace)
   :synchronizing?   (r engine/synchronizing?       db extension-id item-namespace)
   :select-mode?     (get-in db [extension-id :lister-meta :select-mode?])})

(a/reg-sub ::get-body-props get-body-props)



;; -- Search-mode header components -------------------------------------------
;; ----------------------------------------------------------------------------

(defn- quit-search-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyw) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::quit-search-mode-button
                   {:on-click [:item-lister/toggle-search-mode! extension-id]
                    :preset   :menu-bar-icon-button}])

(defn- search-items-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
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

(defn- toggle-search-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::toggle-search-mode-button
                   {:on-click [:item-lister/toggle-search-mode! extension-id]
                    :preset   :search-icon-button
                    :tooltip  :search}])

(defn- search-block
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:viewport-small? (boolean)(opt)
  ;    Default: false}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [viewport-small?]}]
  (if (boolean viewport-small?)
      [toggle-search-mode-button extension-id item-namespace]
      [search-items-field        extension-id item-namespace]))



;; -- Select-mode header components -------------------------------------------
;; ----------------------------------------------------------------------------

(defn- quit-select-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyw) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::quit-select-mode-button
                   {:on-click [:item-lister/toggle-select-mode! extension-id]
                    :preset   :menu-bar-icon-button}])

(defn- select-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::select-all-items-button
                   {:on-click [:item-lister/select-all-items! extension-id]
                    :preset   :default-icon-button
                    :tooltip  :check-all
                    :icon     :check_box_outline_blank}])

(defn- delete-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::delete-selected-items-button
                   {:on-click [:item-lister/delete-selected-items! extension-id]
                    :preset   :delete-icon-button
                    :tooltip  :delete!}])

(defn- duplicate-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::duplicate-selected-items-button
                   {:on-click [:item-lister/duplicate-selected-items! extension-id]
                    :preset   :duplicate-icon-button
                    :tooltip  :copy!}])



;; -- Actions-mode header components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn- new-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [new-item-uri (engine/new-item-uri extension-id item-namespace)]
       [elements/button ::new-item-button
                        {:on-click [:router/go-to! new-item-uri]
                         :preset   :add-icon-button
                         :tooltip  :add-new!}]))

(defn- new-item-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:options (vector)}
  ;
  ; @usage
  ;  (a/reg-event-fx :my-extension/add-new-my-type! (fn [_ [_ selected-option]] ...))
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

(defn- toggle-select-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::toggle-select-mode-button
                   {:on-click [:item-lister/toggle-select-mode! extension-id]
                    :preset   :select-mode-icon-button
                    :tooltip  :check}])

(defn- toggle-item-filter-visibility-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::toggle-item-filter-visibility-button
                   {:on-click [:item-lister/toggle-item-filter-visibility! extension-id]
                    :preset   :filters-icon-button
                    :tooltip  :filters}])

(defn- sort-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:initial-value (keyword)
  ;   :options (vector)}
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



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- search-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id item-namespace _ _]
  [:div.item-lister--header--menu-bar
    [search-items-field      extension-id item-namespace]
    [quit-search-mode-button extension-id]])

(defn- select-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id _ _ _]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group
      [select-all-items-button         extension-id]
      [duplicate-selected-items-button extension-id]
      [delete-selected-items-button    extension-id]]
    [quit-select-mode-button extension-id]])

(defn- actions-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id item-namespace _ _]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group
      [new-item-button                      extension-id item-namespace]
      [sort-items-button                    extension-id item-namespace
                                            {:options       engine/DEFAULT-ORDER-BY-OPTIONS
                                             :initial-value engine/DEFAULT-ORDER-BY}]
      [toggle-select-mode-button            extension-id]
      [toggle-item-filter-visibility-button extension-id]
      ; TEMP
      [toggle-search-mode-button extension-id item-namespace]]
    [:div.item-lister--header--menu-item-group
      [search-block extension-id item-namespace]]])

(defn- header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id item-namespace lister-props {:keys [search-mode? select-mode?] :as header-props}]
  [:div#item-lister--header--structure
    [react-transition/mount-animation {:animation-timeout 500 :mounted? (and search-mode? (not select-mode?))}
                                      [search-mode-header  extension-id item-namespace lister-props header-props]]
    [react-transition/mount-animation {:animation-timeout 500 :mounted? (and select-mode? (not search-mode?))}
                                      [select-mode-header  extension-id item-namespace lister-props header-props]]
    [react-transition/mount-animation {:animation-timeout 500 :mounted? (and (not search-mode?) (not select-mode?))}
                                      [actions-mode-header extension-id item-namespace lister-props header-props]]])

(defn header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;
  ; @return (component)
  [extension-id item-namespace lister-props]
  (let [header-props (a/subscribe [::get-header-props extension-id])]
       (fn [] [header-structure extension-id item-namespace lister-props @header-props])))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request-indicator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ; @param (map) body-props
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
  ; @param (map) body-props
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
  ; @param (map) body-props
  ;
  ; @return (component)
  [extension-id item-namespace lister-props body-props]
  [sortable {}])

(defn- non-sortable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;  {:list-element (component)
  ;   :common-props (map)(opt)}
  ; @param (map) body-props
  ;  {:downloaded-items (vector)
  ;   :select-mode? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [extension-id item-namespace {:keys [list-element]                  :as lister-props}
                               {:keys [downloaded-items select-mode?] :as   body-props}]
  [:div.item-lister--item-list
    (map-indexed (fn [item-dex item]
                     (if (boolean select-mode?)
                        ; If select-mode is enabled ...
                        ^{:key (db/document->document-id item)}
                         [:div.item-lister--item-list--selectable-list-item
                           [elements/checkbox {:color :primary :indent :both}]
                           [list-element item-dex item]]
                        ; If select-mode is NOT enabled ...
                        ^{:key (db/document->document-id item)}
                         [list-element item-dex item]))
                 (param downloaded-items))])

(defn- item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ; @param (map) body-props
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [sortable?] :as lister-props} body-props]
  (if (boolean sortable?)
      [sortable-item-list     extension-id item-namespace lister-props body-props]
      [non-sortable-item-list extension-id item-namespace lister-props body-props]))

(defn- item-lister-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;  {:sortable? (boolean)(opt)}
  ; @param (map) body-props
  ;  {:downloaded-items (vector)}
  ;
  ; @return (hiccup)
  [extension-id item-namespace {:keys [sortable?]        :as lister-props}
                               {:keys [downloaded-items] :as   body-props}]
  [:div.item-lister--structure
    [item-list             extension-id item-namespace lister-props body-props]
    [tools/infinite-loader extension-id {:on-viewport [:item-lister/request-items! extension-id item-namespace]}]
    [request-indicator     extension-id item-namespace lister-props body-props]
    ; "No items to show" label
    (if (empty? downloaded-items)
        [no-items-to-show-label extension-id item-namespace lister-props body-props])])

(defn body
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
  ;  [item-lister/body :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-list-element [item-dex item] [:div ...])
  ;  [item-lister/body :my-extension :my-type {:element #'my-list-element}]
  ;
  ; @usage
  ;  (defn my-list-element [item-dex item ] [:div ...])
  ;  [item-lister/body :my-extension :my-type {:element     #'my-list-element
  ;                                            :item-groups [:my-type :your-type]}]
  ;
  ; @return (component)
  [extension-id item-namespace lister-props]
  (let [body-props (a/subscribe [::get-body-props extension-id item-namespace])]
       (fn [] [item-lister-structure extension-id item-namespace lister-props @body-props])))
