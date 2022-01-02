
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.4.8
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.views
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.loop      :refer [reduce-indexed]]
              [mid-fruits.vector    :as vector]
              [x.app-core.api       :as a :refer [r]]
              [x.app-components.api :as components]
              [x.app-elements.api   :as elements]
              [x.app-layouts.api    :as layouts]
              [x.app-tools.api      :as tools]
              [app-fruits.react-transition    :as react-transition]
              [app-plugins.item-lister.engine :as engine]))
             ;[app-plugins.sortable.core      :refer [sortable]]



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Search-mode header components -------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-search-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyw) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [synchronizing?]}]
  [elements/button ::quit-search-mode-button
                   {:disabled? synchronizing?
                    :on-click  [:item-lister/toggle-search-mode! extension-id]
                    :preset    :close-icon-button}])

(defn search-items-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [synchronizing?]}]
  [elements/search-field ::search-items-field
                         {:auto-focus?   true
                          :disabled?     synchronizing?
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
  ;  {:synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [synchronizing?]}]
  [elements/button ::toggle-search-mode-button
                   {:disabled? synchronizing?
                    :on-click  [:item-lister/toggle-search-mode! extension-id]
                    :preset    :search-icon-button
                    :tooltip   :search}])

(defn search-block
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:viewport-small? (boolean)(opt)}
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
  ; @param (keyw) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [synchronizing?]}]
  [elements/button ::quit-select-mode-button
                   {:disabled? synchronizing?
                    :on-click  [:item-lister/toggle-select-mode! extension-id]
                    :preset    :close-icon-button}])

(defn unselect-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [synchronizing?]}]
  [elements/button ::unselect-all-items-button
                   {:disabled? synchronizing?
                    :on-click  [:item-lister/unselect-all-items! extension-id]
                    :preset    :default-icon-button
                    :icon      :check_box}])

(defn unselect-some-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [synchronizing?]}]
  [elements/button ::unselect-some-items-button
                   {:disabled? synchronizing?
                    :on-click  [:item-lister/unselect-all-items! extension-id]
                    :preset    :default-icon-button
                    :icon      :indeterminate_check_box}])

(defn select-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [synchronizing?]}]
  [elements/button ::select-all-items-button
                   {:disabled? synchronizing?
                    :on-click  [:item-lister/select-all-items! extension-id]
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
  ;   :synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [any-item-selected? synchronizing?]}]
  [elements/button ::delete-selected-items-button
                   {:disabled? (-> any-item-selected? not (or synchronizing?))
                    :on-click  [:item-lister/delete-selected-items! extension-id item-namespace]
                    :preset    :delete-icon-button
                    :tooltip   :delete!}])



;; -- Reorder-mode header components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-order-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:order-changed? (boolean)(opt)
  ;   :synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [order-changed? synchronizing?]}]
  [elements/button ::save-order-button
                   {:disabled? (-> order-changed? not (or synchronizing?))
                    :on-click  [:item-lister/save-order! extension-id]
                    :label     :save-order!
                    :preset    :primary-button}])

(defn quit-reorder-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyw) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [synchronizing?]}]
  [elements/button ::quit-reorder-mode-button
                   {:disabled? synchronizing?
                    :on-click  [:item-lister/toggle-reorder-mode! extension-id]
                    :preset    :close-icon-button}])



;; -- Actions-mode header components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item-button
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

(defn new-item-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:new-item-options (vector)}
  ;
  ; @usage
  ;  (a/reg-event-fx :my-extension/add-new-item! (fn [_ [_ selected-option]] ...))
  ;  [item-lister/new-item-select :my-extension :my-type {:options [...]}]
  ;
  ; @return (component)
  [extension-id _ {:keys [new-item-options]}]
  [elements/select ::new-item-select
                   {:as-button?      true
                    :autoclear?      true
                    :initial-options (param new-item-options)
                    :on-select       (engine/add-new-item-event extension-id)
                    :preset          :add-icon-button}])

(defn toggle-select-mode-button
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

(defn toggle-reorder-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:no-items-to-show? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id _ {:keys [no-items-to-show?]}]
  [elements/button ::toggle-reorder-mode-button
                   {:disabled? no-items-to-show?
                    :on-click  [:item-lister/toggle-reorder-mode! extension-id]
                    :preset    :reorder-mode-icon-button
                    :tooltip   :reorder}])

(defn sort-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [no-items-to-show?]}]
  [elements/select ::sort-items-button
                   {:as-button?    true
                    :disabled?     no-items-to-show?
                    :on-select     [:item-lister/order-items! extension-id item-namespace]
                    :options-label :order-by
                    :preset        :order-by-icon-button
                    :tooltip       :order-by
                    :options-path  [extension-id :item-lister/meta-items :order-by-options]
                    :value-path    [extension-id :item-lister/meta-items :order-by]}])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn search-mode-header
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

(defn select-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id item-namespace header-props]
  ; {:select-mode? true} beállítással, amikor egyszerre több elemet lehetséges kijelölni, nem
  ; szükséges duplikálás gombot elhelyezni. Nem életszerű a több elem egyidejű duplikálása, illetve
  ; nagyon nehezen megoldható a létrejövő elemek (amelyek a szerveren kapnak azonosítót) elhelyezése
  ; a listában úgy, hogy a pozíciójuk megfeleljen beállított rendezési és keresési elveknek.
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group
      [toggle-all-items-selection-button extension-id item-namespace header-props]
      [delete-selected-items-button      extension-id item-namespace header-props]]
    [quit-select-mode-button extension-id item-namespace header-props]])

(defn actions-mode-header
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
                           [new-item-button extension-id item-namespace])
      [sort-items-button          extension-id item-namespace header-props]
      [toggle-select-mode-button  extension-id item-namespace header-props]
      [toggle-reorder-mode-button extension-id item-namespace header-props]]
    [:div.item-lister--header--menu-item-group
      [search-block extension-id item-namespace header-props]]])

(defn reorder-mode-header
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

(defn header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:actions-mode? (boolean)(opt)
  ;   :reorder-mode? (boolean)(opt)
  ;   :search-mode? (boolean)(opt)
  ;   :select-mode? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [actions-mode? reorder-mode? search-mode? select-mode?] :as header-props}]
  [:div#item-lister--header--structure
    [react-transition/mount-animation {:animation-timeout 500 :mounted? actions-mode?}
                                      [actions-mode-header extension-id item-namespace header-props]]
    [react-transition/mount-animation {:animation-timeout 500 :mounted? search-mode?}
                                      [search-mode-header  extension-id item-namespace]]
    [react-transition/mount-animation {:animation-timeout 500 :mounted? select-mode?}
                                      [select-mode-header  extension-id item-namespace header-props]]
    [react-transition/mount-animation {:animation-timeout 500 :mounted? reorder-mode?}
                                      [reorder-mode-header extension-id item-namespace header-props]]])

(defn header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) header-props
  ;  {:new-item-options (vector)(opt)}
  ;
  ; @usage
  ;  [item-lister/header :my-extension :my-type]
  ;
  ; @usage
  ;  [item-lister/header :my-extension :my-type {:new-item-options [:add-my-type! :add-your-type!]}]
  ;
  ; @return (component)
  ([extension-id item-namespace]
   [header extension-id item-namespace {}])

  ([extension-id item-namespace header-props]
   (let [subscribed-props (a/subscribe [:item-lister/get-header-props extension-id item-namespace])]
        (fn [] [header-structure extension-id item-namespace (merge header-props @subscribed-props)]))))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-indicator-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (component)
  [_ _ _]
  [elements/label {:content :downloading-items... :font-size :xs :color :highlight :font-weight :bold}])

(defn request-indicator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:downloading-items? (boolean)(opt)
  ;   :synchronized? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [downloading-items? synchronized?] :as body-props}]
  (if (or (boolean downloading-items?)
          ; Az adatok letöltésének megkezdése előtti pillanatban nem jelenne meg a request-indicator
          ; felirat és a tartalmazó elem magassága egy rövid pillanatra összeugrana a következő
          ; feltétel hozzáadása nélkül:
          (and (not downloading-items?)
               (not synchronized?)))
      [elements/row {:content [request-indicator-label extension-id item-namespace body-props]
                     :horizontal-align :center}]))

(defn no-items-to-show-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (component)
  [_ _ _]
  [elements/label {:content :no-items-to-show :font-size :xs :color :highlight :font-weight :bold}])

(defn no-items-to-show
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:downloading-items? (boolean)(opt)
  ;   :no-items-to-show? (boolean)(opt)
  ;   :synchronized? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [downloading-items? no-items-to-show? synchronized?] :as body-props}]
  (if (and (boolean no-items-to-show?)
           ; Szükséges a synchronized? értékét is vizsgálni, hogy az adatok letöltésének elkezdése
           ; előtti pillanatban ne villanjon fel a no-items-to-show-label felirat!
           (boolean synchronized?)
           ; Szükséges a downloading-items? értékét is vizsgálni, hogy az adatok letöltése közben
           ; ne jelenjen meg a no-items-to-show-label felirat!
           (not downloading-items?))
      [elements/row {:content [no-items-to-show-label extension-id item-namespace body-props]
                     :horizontal-align :center}]))

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

(defn list-item-checkbox
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:synchronizing? (boolean)(opt)}
  ; @param (integer) item-dex
  ;
  ; @return (component)
  [extension-id item-namespace body-props item-dex]
  (let [item-selected? (a/subscribe [:item-lister/item-selected? extension-id item-namespace item-dex])]
       (fn [_ _ {:keys [synchronizing?]} item-dex]
           [elements/button {:disabled? synchronizing?
                             :on-click  [:item-lister/toggle-item-selection! extension-id item-namespace item-dex]
                             :preset    (if @item-selected? :checked-icon-button :unchecked-icon-button)}])))

(defn list-item-structure
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
  (let [item-disabled? (a/subscribe [:item-lister/item-disabled? extension-id item-namespace item-dex])]
       (fn [_ _ {:keys [select-mode?] :as body-props} item-dex _]
           [:div.item-lister--list-item--structure
             [:div.item-lister--list-item {:data-disabled @item-disabled?}
                                          [list-element item-dex item]]
             ; - A lista-elem után (és nem előtt) kirenderelt checkbox elem React-fába
             ;   történő csatolása vagy lecsatolása nem okozza a lista-elem újrarenderelését!
             ; - A {:display :flex :flex-direction :row-reverse} tulajdonság beállításával a checkbox
             ;   elem a lista-elem előtt jelenik meg.
             (if select-mode? [list-item-checkbox extension-id item-namespace body-props item-dex])])))

(defn selectable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [downloaded-items] :as body-props}]
  (reduce-indexed (fn [item-list item-dex {:keys [id] :as item}]
                      (conj item-list
                            ; A lista-elemek React-kulcsának tartalmaznia kell az adott elem indexét,
                            ; hogy a lista-elemek törlésekor a megmaradó elemek alkalmazkodjanak
                            ; az új indexükhöz!
                           ^{:key (str id item-dex)}
                            [list-item-structure extension-id item-namespace body-props item-dex item]))
                  [:div.item-lister--item-list]
                  (param downloaded-items)))

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

(defn item-lister-structure
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
    [request-indicator     extension-id item-namespace body-props]
    [no-items-to-show      extension-id item-namespace body-props]])

(defn body
  ; WARNING!
  ; A body komponens nem kaphatja meg paraméterként a listaelemek számára átadandó közös
  ; paraméter térképet (common-props)!
  ; Ha a body komponens paraméterként kapná meg a common-props térképet, akkor a common-props
  ; megváltozása a teljes body komponens újrarendelődésével járna, ami az infinite-loader
  ; komponens újratöltését okozná!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:list-element (component)}
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
  ;  [item-lister/body :my-extension :my-type {:element #'my-list-element}]
  ;
  ; @return (component)
  [extension-id item-namespace body-props]
  (let [subscribed-props (a/subscribe [:item-lister/get-body-props extension-id item-namespace])]
       (fn [] [item-lister-structure extension-id item-namespace (merge body-props @subscribed-props)])))



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [extension-id item-namespace {:keys [description] :as view-props}]
  [layouts/layout-a extension-id {:body   {:content [body   extension-id item-namespace view-props]}
                                  :header {:content [header extension-id item-namespace view-props]}
                                  :description description}])

(defn view
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) view-props
  ;  {:list-element (component)
  ;   :new-item-options (vector)(opt)}
  ;
  ; @usage
  ;  [item-lister/view :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-list-element [item-dex item] [:div ...])
  ;  [item-lister/view :my-extension :my-type {:list-element #'my-list-element
  ;                                            :new-item-options [:add-my-type! :add-your-type!]}]
  ;
  ; @return (component)
  [extension-id item-namespace view-props]
  (let [subscribed-props (a/subscribe [:item-lister/get-view-props extension-id item-namespace])]
       (fn [] [layout extension-id item-namespace (merge view-props @subscribed-props)])))
