
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
              [app-fruits.react-transition    :as react-transition]
              [app-plugins.item-lister.engine :as engine]
              [app-plugins.sortable.core      :refer [sortable]]))



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

(defn- unselect-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;
  ; @return (component)
  [extension-id _ _]
  [elements/button ::unselect-all-items-button
                   {:on-click [:item-lister/unselect-all-items! extension-id]
                    :preset   :default-icon-button
                    :icon     :check_box}])

(defn- unselect-some-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;
  ; @return (component)
  [extension-id _ _]
  [elements/button ::unselect-some-items-button
                   {:on-click [:item-lister/unselect-all-items! extension-id]
                    :preset   :default-icon-button
                    :icon     :indeterminate_check_box}])

(defn- select-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;
  ; @return (component)
  [extension-id _ _]
  [elements/button ::select-all-items-button
                   {:on-click [:item-lister/select-all-items! extension-id]
                    :preset   :default-icon-button
                    :icon     :check_box_outline_blank}])

(defn- toggle-all-items-selection-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:all-items-selected? (boolean)(opt)
  ;   :any-item-selected? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [all-items-selected? any-item-selected?] :as element-props}]
  (cond all-items-selected? [unselect-all-items-button  extension-id item-namespace element-props]
        any-item-selected?  [unselect-some-items-button extension-id item-namespace element-props]
        :no-items-selected  [select-all-items-button    extension-id item-namespace element-props]))

(defn- delete-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:any-item-selected? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [any-item-selected?]}]
  [elements/button ::delete-selected-items-button
                   {:disabled? (not any-item-selected?)
                    :on-click  [:item-lister/render-delete-selected-items-dialog! extension-id]
                    :preset    :delete-icon-button
                    :tooltip   :delete!}])

(defn- duplicate-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;  {:any-item-selected? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [any-item-selected?]}]
  [elements/button ::duplicate-selected-items-button
                   {:disabled? (not any-item-selected?)
                    :on-click  [:item-lister/render-duplicate-selected-items-dialog! extension-id]
                    :preset    :duplicate-icon-button
                    :tooltip   :copy!}])



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
                    :tooltip  :select}])

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
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id item-namespace _]
  [:div.item-lister--header--menu-bar
    [search-items-field      extension-id item-namespace]
    [quit-search-mode-button extension-id]])

(defn- select-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id item-namespace header-props]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group
      [toggle-all-items-selection-button extension-id item-namespace header-props]
      [duplicate-selected-items-button   extension-id item-namespace header-props]
      [delete-selected-items-button      extension-id item-namespace header-props]]
    [quit-select-mode-button extension-id]])

(defn- actions-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id item-namespace header-props]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group
      [new-item-button                      extension-id item-namespace]
      [sort-items-button                    extension-id item-namespace
                                            {:options       engine/DEFAULT-ORDER-BY-OPTIONS
                                             :initial-value engine/DEFAULT-ORDER-BY}]
      [toggle-select-mode-button            extension-id]]
    ; TODO ...
    ; [toggle-item-filter-visibility-button extension-id]
    [:div.item-lister--header--menu-item-group
      [search-block extension-id item-namespace header-props]]])

(defn- header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [search-mode? select-mode?] :as header-props}]
  [:div#item-lister--header--structure
    [react-transition/mount-animation {:animation-timeout 500 :mounted? (and search-mode? (not select-mode?))}
                                      [search-mode-header  extension-id item-namespace header-props]]
    [react-transition/mount-animation {:animation-timeout 500 :mounted? (and select-mode? (not search-mode?))}
                                      [select-mode-header  extension-id item-namespace header-props]]
    [react-transition/mount-animation {:animation-timeout 500 :mounted? (and (not search-mode?) (not select-mode?))}
                                      [actions-mode-header extension-id item-namespace header-props]]])

(defn header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) header-props
  ;
  ; @return (component)
  ([extension-id item-namespace]
   [header extension-id item-namespace {}])

  ([extension-id item-namespace header-props]
   (let [subscribed-props (a/subscribe [:item-lister/get-header-props extension-id])]
        (fn [] [header-structure extension-id item-namespace (merge header-props @subscribed-props)]))))



;; -- List-item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn static-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) item-props
  ;  {:on-click (metamorphic-event)}
  ;
  ; @return (hiccup)
  [extension-id item-namespace {:keys [on-click] :as item-props}]
  [:div.x-item-lister--item-list--static-list-item
     [components/content item-props]])

(defn toggle-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) item-props
  ;  {:on-click (metamorphic-event)}
  ;
  ; @return (hiccup)
  [extension-id item-namespace {:keys [on-click] :as item-props}]
  [:button.x-item-lister--item-list--toggle-list-item
     {:on-click #(a/dispatch on-click)}
     [components/content item-props]])

(defn list-item
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) item-props
  ;  {:content (metamorphic-content)
  ;   :content-props (map)(opt)
  ;   :on-click (metamorphic-event)(opt)
  ;   :subscriber (subscription vector)(opt)}
  ;
  ; @usage
  ;  [item-lister/list-item :my-extension :my-type {...}]
  ;
  ; @return (hiccup)
  [extension-id item-namespace {:keys [on-click] :as item-props}]
  (if (some? on-click) [toggle-list-item extension-id item-namespace item-props]
                       [static-list-item extension-id item-namespace item-props]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request-indicator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:synchronizing? (boolean)}
  ;
  ; @return (component)
  [_ _ {:keys [synchronizing?]}]
  (if (boolean synchronizing?)
      [:div.item-lister--items--request-indicator [components/content {:content :downloading-items...}]]))

(defn- no-items-to-show-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:synchronizing? (boolean)}
  ;
  ; @return (component)
  [_ _ {:keys [downloaded-items synchronizing?]}]
  (if (and (empty? downloaded-items)
           (not    synchronizing?))
      [:div.item-lister--items--no-items-to-show [components/content {:content :no-items-to-show}]]))

(defn- sortable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (component)
  [extension-id item-namespace body-props]
  [sortable {}])

(defn- list-item-checkbox
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @return (component)
  [extension-id _ item-dex]
  [elements/checkbox {:color :primary :indent :both
                      :value-path [extension-id :lister-meta :item-selections item-dex]}])

(defn- non-sortable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:downloaded-items (vector)
  ;   :list-element (component)
  ;   :select-mode? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [extension-id item-namespace {:keys [downloaded-items list-element select-mode?] :as body-props}]
  [:div.item-lister--item-list (map-indexed (fn [item-dex item]
                                                (if (boolean select-mode?)
                                                   ; If select-mode is enabled ...
                                                   ^{:key (db/document->document-id item)}
                                                    [:div.item-lister--item-list--selectable-list-item
                                                      [list-item-checkbox extension-id item-namespace item-dex]
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
  ; @param (map) body-props
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [sortable?] :as body-props}]
  (if sortable? [sortable-item-list     extension-id item-namespace body-props]
                [non-sortable-item-list extension-id item-namespace body-props]))

(defn- item-lister-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:downloaded-items (vector)
  ;   :sortable? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [extension-id item-namespace {:keys [downloaded-items sortable?] :as body-props}]
  [:div.item-lister--structure
    [item-list              extension-id item-namespace body-props]
    [tools/infinite-loader  extension-id {:on-viewport [:item-lister/request-items! extension-id item-namespace]}]
    [request-indicator      extension-id item-namespace body-props]
    [no-items-to-show-label extension-id item-namespace body-props]])

(defn body
  ; WARNING!
  ;  Az item-lister paraméterként nem kaphatja meg a listaelemek számára átadandó közös
  ;  paraméter térképet (common-props)!
  ;  Ha az item-lister paraméterként kapná meg a common-props értékét, akkor a common-props
  ;  megváltozása az item-lister újrarendelődésével járna, ami az infinite-loader
  ;  komponens újratöltését okozná!
  ;
  ;  Pl.: clients modul, client-list nézet
  ;       Megváltozik a kiválasztott nyelv -> újrarenderelődne a lista
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:list-element (component)
  ;   :item-groups (keywords in vector)(opt)
  ;    TODO ...}
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
  [extension-id item-namespace body-props]
  (let [subscribed-props (a/subscribe [:item-lister/get-body-props extension-id item-namespace])]
       (fn [] [item-lister-structure extension-id item-namespace (merge body-props @subscribed-props)])))
