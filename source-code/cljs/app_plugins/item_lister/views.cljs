
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.0
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.views
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.logical   :refer [nor]]
              [mid-fruits.loop      :refer [reduce-indexed]]
              [x.app-core.api       :as a :refer [r]]
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
  [elements/button :item-lister/quit-search-mode-button
                   {:on-click [:item-lister/toggle-search-mode! extension-id]
                    :preset   :close-icon-button
                    :keypress {:key-code 27}}])

(defn search-items-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:lister-disabled? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [lister-disabled?]}]
  [elements/search-field ::search-items-field
                         {:auto-focus?   true
                          :disabled?     lister-disabled?
                          :layout        :row
                          :min-width     :xs
                          :on-empty      [:item-lister/search-items! extension-id item-namespace]
                          :on-type-ended [:item-lister/search-items! extension-id item-namespace]
                          :placeholder   :search
                          :value-path    [extension-id :item-lister/meta-items :search-term]}])

(defn toggle-search-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:lister-disabled? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [lister-disabled?]}]
  [elements/button ::toggle-search-mode-button
                   {:disabled? lister-disabled?
                    :on-click  [:item-lister/toggle-search-mode! extension-id]
                    :preset    :search-icon-button
                    :tooltip   :search}])

(defn search-block
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:viewport-small? (boolean)(opt)}
  ;
  ; @usage
  ;  [item-lister/search-block :my-extension :my-type {...}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [viewport-small?] :as element-props}]
  (if viewport-small? [toggle-search-mode-button extension-id item-namespace element-props]
                      [search-items-field        extension-id item-namespace element-props]))



;; -- Select-mode header components -------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-select-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:lister-disabled? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [lister-disabled?]}]
  [elements/button ::quit-select-mode-button
                   {:disabled? lister-disabled?
                    :on-click  [:item-lister/toggle-select-mode! extension-id]
                    :preset    :close-icon-button
                    :keypress  {:key-code 27}}])

(defn unselect-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:lister-disabled? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [lister-disabled?]}]
  [elements/button ::unselect-all-items-button
                   {:disabled? lister-disabled?
                    :on-click  [:item-lister/unselect-all-items! extension-id]
                    :preset    :default-icon-button
                    :icon      :check_box}])

(defn unselect-some-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:lister-disabled? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [lister-disabled?]}]
  [elements/button ::unselect-some-items-button
                   {:disabled? lister-disabled?
                    :on-click  [:item-lister/unselect-all-items! extension-id]
                    :preset    :default-icon-button
                    :icon      :indeterminate_check_box}])

(defn select-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:lister-disabled? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [lister-disabled?]}]
  [elements/button ::select-all-items-button
                   {:disabled? lister-disabled?
                    :on-click  [:item-lister/select-all-items! extension-id item-namespace]
                    :preset    :default-icon-button
                    :icon      :check_box_outline_blank}])

(defn toggle-all-items-selection-button
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

(defn delete-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:any-item-selected? (boolean)(opt)
  ;   :lister-disabled? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [any-item-selected? lister-disabled?]}]
  [elements/button ::delete-selected-items-button
                   {:disabled? (-> any-item-selected? not (or lister-disabled?))
                    :on-click  [:item-lister/delete-selected-items! extension-id item-namespace]
                    :preset    :delete-icon-button
                    :tooltip   :delete!}])

(defn duplicate-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:any-item-selected? (boolean)(opt)
  ;   :lister-disabled? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [any-item-selected? lister-disabled?]}]
  [elements/button ::duplicate-selected-items-button
                   {:disabled? (-> any-item-selected? not (or lister-disabled?))
                    :on-click  [:item-lister/duplicate-selected-items! extension-id item-namespace]
                    :preset    :duplicate-icon-button
                    :tooltip   :duplicate!}])



;; -- Reorder-mode header components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-order-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:order-changed? (boolean)(opt)
  ;   :lister-disabled? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [order-changed? lister-disabled?]}]
  [elements/button ::save-order-button
                   {:disabled? (-> order-changed? not (or lister-disabled?))
                    :on-click  [:item-lister/save-order! extension-id]
                    :label     :save-order!
                    :preset    :primary-button}])

(defn quit-reorder-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:lister-disabled? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [lister-disabled?]}]
  [elements/button ::quit-reorder-mode-button
                   {:disabled? lister-disabled?
                    :on-click  [:item-lister/toggle-reorder-mode! extension-id]
                    :preset    :close-icon-button
                    :keypress  {:key-code 27}}])



;; -- Menu-mode header components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;  {:lister-disabled? (boolean)(opt)}
  ;
  ; @usage
  ;  [item-lister/new-item-button :my-extension :my-type {...}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [lister-disabled?]}]
  (let [new-item-uri (engine/new-item-uri extension-id item-namespace)]
       [elements/button ::new-item-button
                        {:disabled? lister-disabled?
                         :on-click  [:router/go-to! new-item-uri]
                         :preset    :primary-icon-button
                         :tooltip   :add-new!
                         :icon      :add_circle}]))

(defn new-item-select
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:lister-disabled? (boolean)(opt)
  ;   :new-item-options (vector)}
  ;
  ; @usage
  ;  (a/reg-event-fx :my-extension/add-new-item! (fn [_ [_ selected-option]] ...))
  ;  [item-lister/new-item-select :my-extension :my-type {:options [...]}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [lister-disabled? new-item-options]}]
  [elements/select ::new-item-select
                   {:as-button?      true
                    :autoclear?      true
                    :disabled?       lister-disabled?
                    :initial-options (param new-item-options)
                    :on-select       (engine/add-new-item-event extension-id item-namespace)
                    :preset          :primary-icon-button
                    :tooltip         :add-new!
                    :icon            :add_circle}])

(defn toggle-select-mode-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:lister-disabled? (boolean)(opt)
  ;   :no-items-to-show? (boolean)(opt)
  ;   :selectable? (boolean)(opt)}
  ;
  ; @usage
  ;  [item-lister/toggle-select-mode-button :my-extension :my-type {...}]
  ;
  ; @return (component)
  [extension-id _ {:keys [lister-disabled? no-items-to-show? selectable?]}]
  (if selectable? [elements/button ::toggle-select-mode-button
                                   {:disabled? (or no-items-to-show? lister-disabled?)
                                    :on-click  [:item-lister/toggle-select-mode! extension-id]
                                    :preset    :select-mode-icon-button
                                    :tooltip   :select}]))

(defn toggle-reorder-mode-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:lister-disabled? (boolean)(opt)
  ;   :no-items-to-show? (boolean)(opt)
  ;   :sortable? (boolean)(opt)}
  ;
  ; @usage
  ;  [item-lister/toggle-reorder-mode-button :my-extension :my-type {...}]
  ;
  ; @return (component)
  [extension-id _ {:keys [lister-disabled? no-items-to-show? sortable?]}]
  (if sortable? [elements/button ::toggle-reorder-mode-button
                                 {:disabled? (or no-items-to-show? lister-disabled?)
                                  :on-click  [:item-lister/toggle-reorder-mode! extension-id]
                                  :preset    :reorder-mode-icon-button
                                  :tooltip   :reorder}]))

(defn sort-items-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:lister-disabled? (boolean)(opt)
  ;   :no-items-to-show? (boolean)(opt)}
  ;
  ; @usage
  ;  [item-lister/sort-items-button :my-extension :my-type {...}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [lister-disabled? no-items-to-show?]}]
  [elements/select ::sort-items-button
                   {:as-button?    true
                    :disabled?     (or no-items-to-show? lister-disabled?)
                    :on-select     [:item-lister/order-items! extension-id item-namespace]
                    :options-label :order-by
                    :preset        :order-by-icon-button
                    :tooltip       :order-by
                    :options-path  [extension-id :item-lister/meta-items :order-by-options]
                    :value-path    [extension-id :item-lister/meta-items :order-by]}])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn search-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;
  ; @return (component)
  [extension-id item-namespace element-props]
  [:div.item-lister--header--menu-bar
    [search-items-field      extension-id item-namespace element-props]
    [quit-search-mode-button extension-id item-namespace element-props]])

(defn search-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:search-mode? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [search-mode?] :as header-props}]
  [react-transition/mount-animation {:animation-timeout 500 :mounted? search-mode?}
                                    [search-mode-header-structure extension-id item-namespace header-props]])

(defn select-mode-header-structure
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
      [delete-selected-items-button      extension-id item-namespace header-props]
      [duplicate-selected-items-button   extension-id item-namespace header-props]]
    [quit-select-mode-button extension-id item-namespace header-props]])

(defn select-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:select-mode? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [select-mode?]}])
  ;(let [selection-props (s [:item-lister/get-selection-props extension-id item-namespace])]
  ;     [react-transition/mount-animation {:animation-timeout 500 :mounted? select-mode?}
  ;                                       [select-mode-header-structure extension-id item-namespace selection-props]]}])

(defn menu-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:new-item-options (vector)(opt)
  ;   :no-items-to-show? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [new-item-options no-items-to-show?] :as header-props}]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group
      (if new-item-options [new-item-select extension-id item-namespace header-props]
                           [new-item-button extension-id item-namespace header-props])
      [sort-items-button          extension-id item-namespace header-props]
      [toggle-select-mode-button  extension-id item-namespace header-props]
      [toggle-reorder-mode-button extension-id item-namespace header-props]]
    [:div.item-lister--header--menu-item-group
      [search-block extension-id item-namespace header-props]]])

(defn menu-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:menu-mode? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [menu-mode?] :as header-props}]
  [react-transition/mount-animation {:animation-timeout 500 :mounted? menu-mode?}
                                    [menu-mode-header-structure extension-id item-namespace header-props]])

(defn reorder-mode-header-structure
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
    [quit-reorder-mode-button extension-id item-namespace header-props]])

(defn reorder-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:reorder-mode? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [reorder-mode?] :as header-props}]
  [react-transition/mount-animation {:animation-timeout 500 :mounted? reorder-mode?}
                                    [reorder-mode-header-structure extension-id item-namespace header-props]])

(defn header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:menu (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [menu] :as header-props}]
  [:div#item-lister--header--structure
    (if menu [menu             extension-id item-namespace header-props]
             [menu-mode-header extension-id item-namespace header-props])
    [reorder-mode-header extension-id item-namespace header-props]
    [select-mode-header  extension-id item-namespace header-props]
    [search-mode-header  extension-id item-namespace header-props]])

(defn header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:menu (metamorphic-content)(opt)
  ;   :new-item-options (vector)(opt)
  ;   :selectable? (boolean)(opt)
  ;    Default: false
  ;   :sortable? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [item-lister/header :my-extension :my-type]
  ;
  ; @usage
  ;  (defn my-menu [extension-id item-namespace header-props] [:div ...])
  ;  [item-lister/header :my-extension :my-type {:menu #'my-menu
  ;                                              :new-item-options [:add-my-type! :add-your-type!]}]
  ;
  ; @return (component)
  [extension-id item-namespace header-props]
  [components/subscriber {:base-props header-props
                          :component  [header-structure              extension-id item-namespace]
                          :subscriber [:item-lister/get-header-props extension-id item-namespace]}])



;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn downloading-items-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) indicator-props
  ;  {:all-items-downloaded? (boolean)(opt)
  ;   :downloading-items? (boolean)(opt)
  ;   :items-received? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [all-items-downloaded? downloading-items? items-received?]}]
  ; - Az adatok letöltésének megkezdése előtti pillanatban is szükséges megjeleníteni
  ;   a downloading-items-label feliratot
  ; - Ha még nincs letöltve az összes elem és várható a downloading-items-label felirat megjelenése,
  ;   addig tartalom nélküli placeholder elemként biztosítja, hogy a felirat megjelenésekor
  ;   és eltűnésekor ne változzon a lista magassága.
  [elements/label {:font-size :xs :color :highlight :font-weight :bold
                   :content (if (or downloading-items? (nor downloading-items? items-received?))
                                :downloading-items...)}])

(defn downloading-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) indicator-props
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [all-items-downloaded? items-received?] :as indicator-props}]
  (if-not (and all-items-downloaded? items-received?) ; XXX#0499
          [elements/row {:content [downloading-items-label extension-id item-namespace indicator-props]
                         :horizontal-align :center}]))

(defn no-items-to-show-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) indicator-props
  ;
  ; @return (component)
  [_ _ _]
  [elements/label {:content :no-items-to-show :font-size :xs :color :highlight :font-weight :bold}])

(defn no-items-to-show
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) indicator-props
  ;  {:downloading-items? (boolean)(opt)
  ;   :items-received? (boolean)(opt)
  ;   :no-items-to-show? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [downloading-items? items-received? no-items-to-show?] :as indicator-props}]
  (if (and (boolean no-items-to-show?)
           ; Szükséges a items-received? értékét is vizsgálni, hogy az adatok letöltésének elkezdése
           ; előtti pillanatban ne villanjon fel a no-items-to-show-label felirat!
           (boolean items-received?)
           ; Szükséges a downloading-items? értékét is vizsgálni, hogy az adatok letöltése közben
           ; ne jelenjen meg a no-items-to-show-label felirat!
           (not downloading-items?))
      [elements/row {:content [no-items-to-show-label extension-id item-namespace indicator-props]
                     :horizontal-align :center}]))

(defn indicators-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) indicator-props
  ;
  ; @return (component)
  [extension-id item-namespace indicator-props]
  [:<> [no-items-to-show  extension-id item-namespace indicator-props]
       [downloading-items extension-id item-namespace indicator-props]])

(defn indicators
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [indicator-props (a/subscribe [:item-lister/get-indicator-props extension-id item-namespace])]
       (fn [] [indicators-structure extension-id item-namespace @indicator-props])))



;; -- List-item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-item-checkbox-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ; @param (map) checkbox-props
  ;  {:checkbox-disabled? (boolean)
  ;   :item-selected? (boolean)
  ;   :select-mode? (boolean)}
  ;
  ; @return (component)
  [extension-id item-namespace item-dex {:keys [checkbox-disabled? item-selected? select-mode?] :as checkbox-props}]
  (if select-mode? [elements/button {:disabled? checkbox-disabled?
                                     :on-click  [:item-lister/toggle-item-selection! extension-id item-namespace item-dex]
                                     :preset    (if item-selected? :checked-icon-button :unchecked-icon-button)}]))

(defn list-item-checkbox
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @return (component)
  [extension-id item-namespace item-dex]
  (let [checkbox-props (a/subscribe [:item-lister/get-checkbox-props extension-id item-namespace item-dex])]
       (fn [] [list-item-checkbox-structure extension-id item-namespace item-dex @checkbox-props])))

(defn list-item-toggle
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:list-element (component)}
  ; @param (integer) item-dex
  ; @param (map) item
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [list-element]} item-dex item]
  (let [on-click [:item-lister/->item-clicked extension-id item-namespace item-dex item]]
       [elements/toggle {:on-click on-click
                         :content [list-element item-dex item]
                         :hover-color :highlight}]))

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
  (let [item-disabled? (a/subscribe [:item-lister/item-disabled? extension-id item-namespace item-dex])]
       (fn [_ _ {:keys [select-mode?] :as body-props} item-dex _]
           [:div.item-lister--list-item--structure
             ; - A lista-elem után (és nem előtt) kirenderelt checkbox elem React-fába
             ;   történő csatolása vagy lecsatolása nem okozza a lista-elem újrarenderelését!
             ; - A {:display :flex :flex-direction :row-reverse} tulajdonságok beállításával a checkbox
             ;   elem a lista-elem előtt jelenik meg.
             [:div.item-lister--list-item {:data-disabled @item-disabled?}
                                          [list-item-toggle extension-id item-namespace body-props item-dex item]]
             [list-item-checkbox extension-id item-namespace item-dex]])))



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
  ; Ha a downloaded-items a get-body-props feliratkozásban lenne, akkor az újonnan letöltött
  ; elemek kirenderelése a meglévő elemek újrarenderelését okozná.
  (let [downloaded-items (a/subscribe [:item-lister/get-downloaded-items extension-id])]
       (fn [] (reduce-kv (fn [item-list item-dex {:keys [id] :as item}]
                             (conj item-list
                                   ; A lista-elemek React-kulcsának tartalmaznia kell az adott elem indexét,
                                   ; hogy a lista-elemek törlésekor a megmaradó elemek alkalmazkodjanak
                                   ; az új indexükhöz!
                                  ^{:key (str id item-dex)}
                                   [list-item-structure extension-id item-namespace body-props item-dex item]))
                         [:div.item-lister--item-list]
                         (param @downloaded-items)))))

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
  ;  {:reorder-mode? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [extension-id item-namespace {:keys [reorder-mode?] :as body-props}]
  (if reorder-mode? [sortable-item-list   extension-id item-namespace body-props]
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
  ;  {:list-element (metamorphic-content)
  ;   :selectable-f (function)(opt)
  ;    Only w/ {:selectable? true}
  ;   :selectable? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [item-lister/body :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-list-element [item-dex item] [:div ...])
  ;  (defn my-item-selectable? [item] true)
  ;  [item-lister/body :my-extension :my-type {:list-element #'my-list-element
  ;                                            :selectable-f my-item-selectable?}]
  ;
  ; @return (component)
  [extension-id item-namespace body-props]
  ; Az [:item-lister/select-all-items! ...] esemény működéséhez szükséges a Re-Frame adatbázisba
  ; írni a {:selectable-f ...} tulajdonság értékét!
  [components/stated {:base-props  body-props
                      :component   [body-structure extension-id item-namespace]
                      :destructor  [:item-lister/unload-lister! extension-id item-namespace]
                      :initializer [:db/set-item! [extension-id :item-lister/meta-items :selectable-f]
                                                  (:selectable-f body-props)]
                      :subscriber  [:item-lister/get-body-props extension-id item-namespace]}])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) view-props
  ;  {:description (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [description] :as view-props}]
  [layouts/layout-a extension-id {:body   [body   extension-id item-namespace view-props]
                                  :header [header extension-id item-namespace view-props]
                                  :description description}])

(defn view
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) view-props
  ;  {:list-element (metamorphic-content)
  ;   :menu (metamorphic-content)(opt)
  ;   :new-item-options (vector)(opt)
  ;   :selectable-f (function)(opt)
  ;    Only w/ {:selectable? true}
  ;   :selectable? (boolean)(opt)
  ;    Default: false
  ;   :sortable? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [item-lister/view :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-list-element [item-dex item] [:div ...])
  ;  (defn my-menu         [extension-id item-namespace header-props] [:div ...])
  ;  (defn my-item-selectable? [item] true)
  ;  [item-lister/view :my-extension :my-type {:list-element #'my-list-element
  ;                                            :menu         #'my-menu
  ;                                            :new-item-options [:add-my-type! :add-your-type!]
  ;                                            :selectable-f my-item-selectable?}]
  ;
  ; @return (component)
  [extension-id item-namespace view-props]
  [components/subscriber {:base-props view-props
                          :component  [view-structure              extension-id item-namespace]
                          :subscriber [:item-lister/get-view-props extension-id item-namespace]}])
