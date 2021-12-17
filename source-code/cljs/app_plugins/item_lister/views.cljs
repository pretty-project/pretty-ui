
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.3.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.views
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.css       :as css]
              [mid-fruits.loop      :refer [reduce-indexed]]
              [mid-fruits.vector    :as vector]
              [x.app-db.api         :as db]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-layouts.api    :as layouts]
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
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::unselect-all-items-button
                   {:on-click [:item-lister/unselect-all-items! extension-id]
                    :preset   :default-icon-button
                    :icon     :check_box}])

(defn- unselect-some-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
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
  [extension-id]
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
  [extension-id _ {:keys [all-items-selected? any-item-selected?]}]
  (cond all-items-selected? [unselect-all-items-button  extension-id]
        any-item-selected?  [unselect-some-items-button extension-id]
        :no-items-selected  [select-all-items-button    extension-id]))

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

(defn- archive-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;  {:any-item-selected? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [any-item-selected?]}]
  [elements/button ::archive-selected-items-button
                   {:disabled? (not any-item-selected?)
                    :on-click  [:item-lister/render-archive-selected-items-dialog! extension-id]
                    :preset    :archive-icon-button
                    :tooltip   :archive!}])



;; -- Reorder-mode header components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn- save-order-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:order-changed? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [order-changed?]}]
  [elements/button ::save-order-button
                   {:disabled? (not order-changed?)
                    :on-click  [:item-lister/save-order! extension-id]
                    :label     :save-order!
                    :preset    :primary-button}])

(defn- quit-reorder-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyw) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::quit-reorder-mode-button
                   {:on-click [:item-lister/toggle-reorder-mode! extension-id]
                    :preset   :menu-bar-icon-button}])



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
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:no-items-to-show? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [no-items-to-show?]}]
  [elements/button ::toggle-select-mode-button
                   {:disabled? no-items-to-show?
                    :on-click  [:item-lister/toggle-select-mode! extension-id]
                    :preset    :select-mode-icon-button
                    :tooltip   :select}])

(defn- toggle-reorder-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:filter-mode? (boolean)(opt)
  ;   :no-items-to-show? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [filter-mode? no-items-to-show?]}]
  [elements/button ::toggle-reorder-mode-button
                   {:disabled? (or filter-mode? no-items-to-show?)
                    :on-click  [:item-lister/toggle-reorder-mode! extension-id]
                    :preset    :reorder-mode-icon-button
                    :tooltip   :reorder}])

(defn- toggle-filter-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:filter-mode? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [filter-mode?]}]
  [elements/button ::toggle-filter-mode-button
                   {:on-click [:item-lister/toggle-filter-mode! extension-id]
                    :preset   (if filter-mode? :filters-visible-icon-button :filters-nonvisible-icon-button)
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
  [extension-id item-namespace {:keys [initial-value no-items-to-show? options]}]
  [elements/select ::sort-items-button
                   {:as-button?      true
                    :disabled?       no-items-to-show?
                    :on-select       [:item-lister/order-items! extension-id item-namespace]
                    :options-label   :order-by
                    :preset          :order-by-icon-button
                    :tooltip         :order-by
                    :initial-value   initial-value
                    :initial-options options
                    :value-path      [extension-id :lister-meta :order-by]}])



;; -- Filter-mode header components -------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-filters
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:filter (keyword)(opt)}
  ;
  ; @return (hiccup)
  [extension-id item-namespace {:keys [filter]}]
  [:div.item-lister--header--item-filters
    [elements/menu-bar {:menu-items [{:label :all-items      :active? (nil? filter)
                                      :on-click [:item-lister/discard-filter! extension-id item-namespace]}
                                     {:label :favorites      :active? (= filter :favorite-items)
                                      :on-click [:item-lister/use-filter! extension-id item-namespace :favorite-items]}
                                     {:label :archived-items :active? (= filter :archived-items)
                                      :on-click [:item-lister/use-filter! extension-id item-namespace :archived-items]}]}]])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- search-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
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
  ; {:select-mode? true} beállítással, amikor egyszerre több elemet lehetséges kijelölni, nem
  ; szükséges duplikálás gombot alkalmazni. Nem életszerű a több elem egyidejű duplikálása, illetve
  ; nagyon nehezen megoldható a létrejövő elemek (amelyek a szerveren kapnak azonosítót) elhelyezése
  ; a listában úgy, hogy a pozíciójuk megfeleljen beállított rendezési és keresési elveknek.
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group
      [toggle-all-items-selection-button extension-id item-namespace header-props]
      [archive-selected-items-button     extension-id item-namespace header-props]
      [delete-selected-items-button      extension-id item-namespace header-props]
      [toggle-filter-mode-button         extension-id item-namespace header-props]]
    [quit-select-mode-button extension-id]])

(defn- actions-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:no-items-to-show? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [no-items-to-show?] :as header-props}]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group
      [new-item-button            extension-id item-namespace]
      [sort-items-button          extension-id item-namespace
                                  {:options       engine/DEFAULT-ORDER-BY-OPTIONS
                                   :initial-value engine/DEFAULT-ORDER-BY
                                   :no-items-to-show? no-items-to-show?}]
      [toggle-select-mode-button  extension-id item-namespace header-props]
      [toggle-reorder-mode-button extension-id item-namespace header-props]
      [toggle-filter-mode-button  extension-id item-namespace header-props]]
    [:div.item-lister--header--menu-item-group
      [search-block extension-id item-namespace header-props]]])

(defn- reorder-mode-header
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
      [elements/button {:layout :icon-button :variant :placeholder}]]
    [:div.item-lister--header--menu-item-group
      [save-order-button extension-id item-namespace header-props]]
    [quit-reorder-mode-button extension-id]])

(defn- header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:actions-mode? (boolean)(opt)
  ;   :filter-mode? (boolean)(opt)
  ;   :reorder-mode? (boolean)(opt)
  ;   :search-mode? (boolean)(opt)
  ;   :select-mode? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [actions-mode? filter-mode? reorder-mode? search-mode? select-mode?] :as header-props}]
  (let [header-height (if filter-mode? 96 48)]
       [:div#item-lister--header--structure {:style {:height (css/px header-height)}}
         [react-transition/mount-animation {:animation-timeout 500 :mounted? actions-mode?}
                                           [actions-mode-header extension-id item-namespace header-props]]
         [react-transition/mount-animation {:animation-timeout 500 :mounted? search-mode?}
                                           [search-mode-header  extension-id item-namespace]]
         [react-transition/mount-animation {:animation-timeout 500 :mounted? select-mode?}
                                           [select-mode-header  extension-id item-namespace header-props]]
         [react-transition/mount-animation {:animation-timeout 500 :mounted? reorder-mode?}
                                           [reorder-mode-header extension-id item-namespace header-props]]
         (if filter-mode?                  [item-filters        extension-id item-namespace header-props])]))

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



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request-indicator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:synchronized? (boolean)(opt)
  ;   :synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [_ _ {:keys [synchronized? synchronizing?]}]
  (cond (boolean synchronizing?)
        [:div.item-lister--request-indicator [components/content {:content :downloading-items...}]]
        ; Az adatok letöltésének megkezdése előtti pillanatban a request-indicator felirat
        ; megjelenése előtt, annak helyén egy placeholder megjelenítése segítségével nem változik
        ; meg a tartalmazó elem magassága, a request-indicator felirat megjelenésekor.
        (and (not synchronizing?)
             (not synchronized?))
        [:div.item-lister--request-indicator [components/content {:content :preparing-to-download...}]]))

(defn- no-items-to-show-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:no-items-to-show? (boolean)(opt)
  ;   :synchronized? (boolean)(opt)
  ;   :synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [_ _ {:keys [no-items-to-show? synchronized? synchronizing?]}]
  (if (and (boolean no-items-to-show?)
           ; Szükséges a synchronized? értékét is vizsgálni, hogy az adatok letöltésének elkezdése
           ; előtti pillanatban ne villanjon fel a no-items-to-show-label felirat!
           (boolean synchronized?)
           ; Szükséges a synchronizing? értékét is vizsgálni, hogy az adatok letöltése közben ne jelenjen
           ; meg a no-items-to-show-label felirat!
           (not synchronizing?))
      [:div.item-lister--no-items-to-show [components/content {:content :no-items-to-show}]]))

(defn- sortable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (component)
  [extension-id item-namespace body-props]
  [:div "sortable"])






(defn- list-item-checkbox
  [extension-id item-dex]
  [:button.item-lister--list-item--checkbox
    {:data-checked false
     :on-click #(a/dispatch [:item-lister/toggle-item-selection! extension-id item-dex])}])





(defn- selectable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [downloaded-items list-element]}]
  (reduce-indexed (fn [item-list {:keys [id] :as item} item-dex]
                      (vector/conj-item item-list ^{:key id}
                                                   [:div.item-lister--list-item--structure
                                                     [list-item-checkbox extension-id item-dex]
                                                     [:div.item-lister--list-item {:data-archived false}
                                                                                  [list-element item-dex item]]]))
                  [:div.item-lister--item-list]
                  (param downloaded-items)))

(defn- default-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [downloaded-items list-element]}]
  (reduce-indexed (fn [item-list {:keys [id] :as item} item-dex]
                      (vector/conj-item item-list ^{:key id}
                                                   [:div.item-lister--list-item [list-element item-dex item]]))
                  [:div.item-lister--item-list]
                  (param downloaded-items)))

(defn- item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:reorder-mode? (boolean)(opt)
  ;   :select-mode? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [extension-id item-namespace {:keys [reorder-mode? select-mode?] :as body-props}]
  (cond select-mode?  [selectable-item-list extension-id item-namespace body-props]
        reorder-mode? [sortable-item-list   extension-id item-namespace body-props]
        :default-mode [default-item-list    extension-id item-namespace body-props]))

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



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [extension-id item-namespace {:keys [description] :as view-props}]
  [layouts/layout-a extension-id {:body   {:content [body   extension-id item-namespace view-props]}
                                  :header {:content [header extension-id item-namespace]}
                                  :description description}])

(defn view
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) view-props
  ;  {:list-element (component)}
  ;
  ; @return (component)
  [extension-id item-namespace view-props]
  (let [subscribed-props (a/subscribe [:item-lister/get-view-props extension-id item-namespace])]
       (fn [] [layout extension-id item-namespace (merge view-props @subscribed-props)])))
