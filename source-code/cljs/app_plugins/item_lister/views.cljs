
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.9.2
; Compatibility: x4.6.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.views
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.logical   :refer [nor]]
              [mid-fruits.vector    :as vector]
              [x.app-core.api       :as a]
              [x.app-components.api :as components]
              [x.app-elements.api   :as elements]
              [x.app-layouts.api    :as layouts]
              [x.app-tools.api      :as tools]
              [app-fruits.react-transition    :as react-transition]
              [app-plugins.item-lister.engine :as engine]))
             ;[app-plugins.sortable.core      :refer [sortable]]



;; -- Search-mode header components -------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-search-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [elements/icon-button :item-lister/quit-search-mode-button
                        {:keypress {:key-code 27} :preset :close
                         :on-click [:item-lister/toggle-search-mode! extension-id item-namespace]}])

(defn search-items-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [error-mode?      @(a/subscribe [:item-lister/error-mode?      extension-id item-namespace])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? extension-id item-namespace])]
       [elements/search-field :item-lister/search-items-field
                              {:auto-focus? true :layout :row :min-width :xs :placeholder :search
                               :disabled?     (or error-mode? lister-disabled?)
                               :on-empty      [:item-lister/search-items! extension-id item-namespace]
                               :on-type-ended [:item-lister/search-items! extension-id item-namespace]
                               :value-path    [extension-id :item-lister/meta-items :search-term]}]))

(defn toggle-search-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [error-mode?      @(a/subscribe [:item-lister/error-mode?      extension-id item-namespace])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? extension-id item-namespace])]
       [elements/icon-button :item-lister/toggle-search-mode-button
                             {:preset :search :tooltip :search
                              :disabled? (or error-mode? lister-disabled?)
                              :on-click  [:item-lister/toggle-search-mode! extension-id item-namespace]}]))

(defn search-block
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-lister/search-block :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
       (if viewport-small? [toggle-search-mode-button extension-id item-namespace]
                           [search-items-field        extension-id item-namespace])))



;; -- Select-mode header components -------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-select-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [elements/icon-button :item-lister/quit-select-mode-button
                        {:keypress {:key-code 27} :preset :close
                         :on-click [:item-lister/toggle-select-mode! extension-id item-namespace]}])


(defn unselect-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? extension-id item-namespace])]
       [elements/icon-button :item-lister/unselect-all-items-button
                             {:icon :check_box :preset :default :disabled? lister-disabled?
                              :on-click [:item-lister/unselect-all-items! extension-id item-namespace]}]))

(defn unselect-some-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? extension-id item-namespace])]
       [elements/icon-button :item-lister/unselect-some-items-button
                             {:icon :indeterminate_check_box :preset :default :disabled? lister-disabled?
                              :on-click [:item-lister/unselect-all-items! extension-id item-namespace]}]))

(defn select-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? extension-id item-namespace])]
       [elements/icon-button :item-lister/select-all-items-button
                             {:icon :check_box_outline_blank :preset :default :disabled? lister-disabled?
                              :on-click [:item-lister/select-all-items! extension-id item-namespace]}]))

(defn toggle-all-items-selection-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [all-items-selected? @(a/subscribe [:item-lister/all-items-selected? extension-id item-namespace])
        any-item-selected?  @(a/subscribe [:item-lister/any-item-selected?  extension-id item-namespace])]
       (cond all-items-selected? [unselect-all-items-button  extension-id item-namespace]
             any-item-selected?  [unselect-some-items-button extension-id item-namespace]
             :no-items-selected  [select-all-items-button    extension-id item-namespace])))

(defn delete-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [item-actions       @(a/subscribe [:item-lister/get-item-actions   extension-id item-namespace])
        lister-disabled?   @(a/subscribe [:item-lister/lister-disabled?   extension-id item-namespace])
        no-items-selected? @(a/subscribe [:item-lister/no-items-selected? extension-id item-namespace])]
       (if (vector/contains-item? item-actions :delete)
           [elements/icon-button :item-lister/delete-selected-items-button
                                 {:preset :delete :tooltip :delete!
                                  :disabled? (or lister-disabled? no-items-selected?)
                                  :on-click  [:item-lister/delete-selected-items! extension-id item-namespace]}])))

(defn duplicate-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [item-actions       @(a/subscribe [:item-lister/get-item-actions   extension-id item-namespace])
        lister-disabled?   @(a/subscribe [:item-lister/lister-disabled?   extension-id item-namespace])
        no-items-selected? @(a/subscribe [:item-lister/no-items-selected? extension-id item-namespace])]
       (if (vector/contains-item? item-actions :duplicate)
           [elements/icon-button :item-lister/duplicate-selected-items-button
                                 {:preset :duplicate :tooltip :duplicate!
                                  :disabled? (or lister-disabled? no-items-selected?)
                                  :on-click  [:item-lister/duplicate-selected-items! extension-id item-namespace]}])))



;; -- Reorder-mode header components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-order-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? extension-id item-namespace])
        order-changed?   @(a/subscribe [:item-lister/order-changed?   extension-id item-namespace])]
       [elements/button :item-lister/save-order-button
                        {:label :save-order! :preset :primary-button
                         :disabled? (or lister-disabled? (not order-changed?))
                         :on-click  [:item-lister/save-order! extension-id item-namespace]}]))

(defn quit-reorder-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [elements/icon-button :item-lister/quit-reorder-mode-button
                        {:keypress {:key-code 27} :preset :close
                         :on-click [:item-lister/toggle-reorder-mode! extension-id item-namespace]}])



;; -- Menu-mode header components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [error-mode?      @(a/subscribe [:item-lister/error-mode?      extension-id item-namespace])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? extension-id item-namespace])
        new-item-uri      (engine/new-item-uri                        extension-id item-namespace)]
       [elements/icon-button :item-lister/new-item-button
                             {:icon :add_circle :preset :primary :tooltip :add-new!
                              :disabled? (or error-mode? lister-disabled?)
                              :on-click  [:router/go-to! new-item-uri]}]))

(defn new-item-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [error-mode?      @(a/subscribe [:item-lister/error-mode?          extension-id item-namespace])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled?     extension-id item-namespace])
        new-item-options @(a/subscribe [:item-lister/get-new-item-options extension-id item-namespace])]
       [elements/select :item-lister/new-item-select
                        {:as-button? true :autoclear? true :icon :add_circle :preset :primary-icon-button :tooltip :add-new!
                         :initial-options new-item-options
                         :disabled?       (or error-mode? lister-disabled?)
                         :on-select       (engine/add-new-item-event extension-id item-namespace)}]))

(defn new-item-block
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:new-item-options (vector)}
  ;
  ; @usage
  ;  [item-lister/new-item-block :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [new-item-options @(a/subscribe [:item-lister/get-new-item-options extension-id item-namespace])]
       (if new-item-options [new-item-select extension-id item-namespace]
                            [new-item-button extension-id item-namespace])))

(defn toggle-select-mode-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-lister/toggle-select-mode-button :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [items-selectable? @(a/subscribe [:item-lister/items-selectable? extension-id item-namespace])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  extension-id item-namespace])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? extension-id item-namespace])]
       (if items-selectable? [elements/icon-button :item-lister/toggle-select-mode-button
                                                   {:preset :select-mode :tooltip :select
                                                    :disabled? (or lister-disabled? no-items-to-show?)
                                                    :on-click  [:item-lister/toggle-select-mode! extension-id item-namespace]}])))

(defn toggle-reorder-mode-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-lister/toggle-reorder-mode-button :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [items-sortable?   @(a/subscribe [:item-lister/items-sortable?   extension-id item-namespace])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  extension-id item-namespace])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? extension-id item-namespace])]
       (if items-sortable? [elements/icon-button :item-lister/toggle-reorder-mode-button
                                                 {:preset :reorder-mode :tooltip :reorder
                                                  :disabled? (or lister-disabled? no-items-to-show?)
                                                  :on-click  [:item-lister/toggle-reorder-mode! extension-id item-namespace]}])))

(defn sort-items-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-lister/sort-items-button :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  extension-id item-namespace])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? extension-id item-namespace])]
       [elements/select :item-lister/sort-items-button
                        {:as-button? true :options-label :order-by :preset :order-by-icon-button :tooltip :order-by
                         :disabled?    (or lister-disabled? no-items-to-show?)
                         :on-select    [:item-lister/order-items! extension-id item-namespace]
                         :options-path [extension-id :item-lister/meta-items :order-by-options]
                         :get-label-f  engine/order-by-label-f
                         :value-path   [extension-id :item-lister/meta-items :order-by]}]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn search-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [:div.item-lister--header--menu-bar [search-items-field      extension-id item-namespace]
                                      [quit-search-mode-button extension-id item-namespace]])

(defn search-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [search-mode? @(a/subscribe [:item-lister/search-mode? extension-id item-namespace])]
       [react-transition/mount-animation {:animation-timeout 500 :mounted? search-mode?}
                                         [search-mode-header-structure extension-id item-namespace]]))

(defn select-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group
      [toggle-all-items-selection-button extension-id item-namespace]
      [delete-selected-items-button      extension-id item-namespace]
      [duplicate-selected-items-button   extension-id item-namespace]]
    [quit-select-mode-button extension-id item-namespace]])

(defn select-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [select-mode? @(a/subscribe [:item-lister/select-mode? extension-id item-namespace])]
       [react-transition/mount-animation {:animation-timeout 500 :mounted? select-mode?}
                                         [select-mode-header-structure extension-id item-namespace]]))

(defn menu-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group [new-item-block             extension-id item-namespace]
                                               [sort-items-button          extension-id item-namespace]
                                               [toggle-select-mode-button  extension-id item-namespace]
                                               [toggle-reorder-mode-button extension-id item-namespace]]
    [:div.item-lister--header--menu-item-group [search-block extension-id item-namespace]]])

(defn menu-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [menu-mode? @(a/subscribe [:item-lister/menu-mode? extension-id item-namespace])]
       [react-transition/mount-animation {:animation-timeout 500 :mounted? menu-mode?}
                                         [menu-mode-header-structure extension-id item-namespace]]))

(defn reorder-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group [elements/button {:layout :icon-button :variant :placeholder}]]
    [:div.item-lister--header--menu-item-group [save-order-button extension-id item-namespace]]
    [quit-reorder-mode-button extension-id item-namespace]])

(defn reorder-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [reorder-mode? @(a/subscribe [:item-lister/reorder-mode? extension-id item-namespace])]
       [react-transition/mount-animation {:animation-timeout 500 :mounted? reorder-mode?}
                                         [reorder-mode-header-structure extension-id item-namespace]]))

(defn header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:menu (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [menu]}]
  [:div#item-lister--header--structure
    (if menu [menu             extension-id item-namespace]
             [menu-mode-header extension-id item-namespace])
    [reorder-mode-header extension-id item-namespace]
    [select-mode-header  extension-id item-namespace]
    [search-mode-header  extension-id item-namespace]])

(defn header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:menu (metamorphic-content)(opt)
  ;   :new-item-options (vector)(opt)}
  ;
  ; @usage
  ;  [item-lister/header :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-menu [extension-id item-namespace] [:div ...])
  ;  [item-lister/header :my-extension :my-type {:menu #'my-menu}}]
  ;
  ; @return (component)
  [extension-id item-namespace header-props]
  (let [state-props  (dissoc      header-props  :menu)
        header-props (select-keys header-props [:menu])]
       [components/stated (engine/component-id extension-id item-namespace :header)
                          {:component   [header-structure extension-id item-namespace header-props]
                           :initializer [:db/apply! [extension-id :item-lister/meta-items] merge state-props]}]))



;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn downloading-items-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  ; - Az adatok letöltésének megkezdése előtti pillanatban is szükséges megjeleníteni
  ;   a downloading-items-label feliratot
  ; - Ha még nincs letöltve az összes elem és várható a downloading-items-label felirat megjelenése,
  ;   addig tartalom nélküli placeholder elemként biztosítja, hogy a felirat megjelenésekor
  ;   és eltűnésekor ne változzon a lista magassága
  (let [downloading-items? @(a/subscribe [:item-lister/downloading-items? extension-id item-namespace])
        items-received?    @(a/subscribe [:item-lister/items-received?    extension-id item-namespace])]
       [elements/label {:font-size :xs :color :highlight :font-weight :bold
                        :content (if (or downloading-items? (nor downloading-items? items-received?))
                                     :downloading-items...)}]))

(defn downloading-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [all-items-downloaded? @(a/subscribe [:item-lister/all-items-downloaded? extension-id item-namespace])
        items-received?       @(a/subscribe [:item-lister/items-received?       extension-id item-namespace])]
       (if-not (and all-items-downloaded? items-received?) ; XXX#0499
               [elements/row {:content [downloading-items-label extension-id item-namespace]
                              :horizontal-align :center}])))

(defn no-items-to-show-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [_ _]
  [elements/label {:content :no-items-to-show :font-size :xs :color :highlight :font-weight :bold}])

(defn no-items-to-show
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [downloading-items? @(a/subscribe [:item-lister/downloading-items? extension-id item-namespace])
        items-received?    @(a/subscribe [:item-lister/items-received?    extension-id item-namespace])
        no-items-to-show?  @(a/subscribe [:item-lister/no-items-to-show?  extension-id item-namespace])]
       (if (and no-items-to-show? items-received?
                ; - Szükséges a items-received? értékét is vizsgálni, hogy az adatok letöltésének elkezdése
                ;   előtti pillanatban ne villanjon fel a no-items-to-show-label felirat!
                ; - Szükséges a downloading-items? értékét is vizsgálni, hogy az adatok letöltése közben
                ;   ne jelenjen meg a no-items-to-show-label felirat!
                (not downloading-items?))
           [elements/row {:content [no-items-to-show-label extension-id item-namespace]
                          :horizontal-align :center}])))

(defn indicators
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (if-let [browser-online? @(a/subscribe [:environment/browser-online?])]
          [:<> [no-items-to-show  extension-id item-namespace]
               [downloading-items extension-id item-namespace]]))



;; -- List-item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-item-checkbox
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @return (component)
  [extension-id item-namespace item-dex]
  (let [item-selected?   @(a/subscribe [:item-lister/item-selected?   extension-id item-namespace item-dex])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? extension-id item-namespace])
        select-mode?     @(a/subscribe [:item-lister/select-mode?     extension-id item-namespace])]
       (if select-mode? [elements/button {:disabled? lister-disabled?
                                          :on-click  [:item-lister/toggle-item-selection! extension-id item-namespace item-dex]
                                          :preset    (if item-selected? :checked-icon-button :unchecked-icon-button)}])))

(defn list-item-toggle
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:list-element (metamorphic-content)}
  ; @param (integer) item-dex
  ; @param (map) item
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [list-element]} item-dex item]
  [elements/toggle {:on-click       [:item-lister/item-clicked       extension-id item-namespace item-dex item]
                    :on-right-click [:item-lister/item-right-clicked extension-id item-namespace item-dex item]
                    :content [list-element extension-id item-namespace item-dex item]
                    :hover-color :highlight}])

(defn list-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ; @param (integer) item-dex
  ; @param (map) item
  ;
  ; @return (component)
  [extension-id item-namespace body-props item-dex item]
  (let [item-disabled? @(a/subscribe [:item-lister/item-disabled? extension-id item-namespace item-dex])]
       [:div.item-lister--list-item--structure
         ; - A lista-elem után (és nem előtt) kirenderelt checkbox elem React-fába
         ;   történő csatolása vagy lecsatolása nem okozza a lista-elem újrarenderelését!
         ; - A {:display :flex :flex-direction :row-reverse} tulajdonságok beállításával a checkbox
         ;   elem a lista-elem előtt jelenik meg.
         [:div.item-lister--list-item {:data-disabled item-disabled?}
                                      [list-item-toggle extension-id item-namespace body-props item-dex item]]
         [list-item-checkbox extension-id item-namespace item-dex]]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selectable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (component)
  [extension-id item-namespace body-props]
  (let [downloaded-items @(a/subscribe [:item-lister/get-downloaded-items extension-id item-namespace])]
       (reduce-kv (fn [item-list item-dex {:keys [id] :as item}]
                      (conj item-list
                            ; A lista-elemek React-kulcsának tartalmaznia kell az adott elem indexét,
                            ; hogy a lista-elemek törlésekor a megmaradó elemek alkalmazkodjanak
                            ; az új indexükhöz!
                           ^{:key (str id item-dex)}
                            [list-item-structure extension-id item-namespace body-props item-dex item]))
                  [:div.item-lister--item-list]
                  (param downloaded-items))))

(defn sortable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (component)
  [extension-id item-namespace body-props]
  [:div "sortable"])
  ; Ne renderelődjenek újra a listaelemek, amikor átvált {:reorder-mode? true} állapotra!

(defn item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ;
  ; @return (hiccup)
  [extension-id item-namespace body-props]
  (if false [sortable-item-list   extension-id item-namespace body-props]
            [selectable-item-list extension-id item-namespace body-props]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (hiccup)
  [extension-id item-namespace body-props]
  [:div.item-lister--body--structure
    [item-list             extension-id item-namespace body-props]
    [tools/infinite-loader extension-id {:on-viewport [:item-lister/request-items! extension-id item-namespace]}]
    [indicators            extension-id item-namespace]])

(defn body
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :list-element (metamorphic-content)
  ;   :on-click (metamorphic-event)(opt)
  ;   :on-load (metamorphic-event)(opt)
  ;   :on-right-click (metamorphic-event)(opt)
  ;   :prefilter (map)(opt)
  ;   :sortable? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [item-lister/body :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-list-element [extension-id item-namespace item-dex item] [:div ...])
  ;  [item-lister/body :my-extension :my-type {:list-element #'my-list-element
  ;                                            :prefilter    {:my-type/color "red"}}]
  ;
  ; @return (component)
  [extension-id item-namespace body-props]
  (let [state-props (dissoc      body-props  :list-element)
        body-props  (select-keys body-props [:list-element])]
       [components/stated (engine/component-id extension-id item-namespace :body)
                          {:component   [body-structure              extension-id item-namespace body-props]
                           :destructor  [:item-lister/unload-lister! extension-id item-namespace]
                           :initializer [:db/apply! [extension-id :item-lister/meta-items] merge state-props]}]))



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) view-props
  ;
  ; @return (component)
  [extension-id item-namespace view-props]
  (let [description @(a/subscribe [:item-lister/get-description extension-id item-namespace])]
       [layouts/layout-a extension-id {:body   [body   extension-id item-namespace view-props]
                                       :header [header extension-id item-namespace view-props]
                                       :description description}]))

(defn view
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) view-props
  ;  {:list-element (metamorphic-content)
  ;   :menu (metamorphic-content)(opt)
  ;   :new-item-options (vector)(opt)
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :prefilter (map)(opt)
  ;   :sortable? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [item-lister/view :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-list-element [extension-id item-namespace item-dex item] [:div ...])
  ;  (defn my-menu         [extension-id item-namespace]               [:div ...])
  ;  [item-lister/view :my-extension :my-type {:list-element #'my-list-element
  ;                                            :menu         #'my-menu
  ;                                            :new-item-options [:add-my-type! :add-your-type!]
  ;                                            :prefilter        {:my-type/color "red"}}]
  ;
  ; @return (component)
  [extension-id item-namespace view-props]
  [view-structure extension-id item-namespace view-props])
