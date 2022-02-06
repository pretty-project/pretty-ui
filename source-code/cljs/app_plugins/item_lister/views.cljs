
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.0
; Compatibility: x4.5.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.views
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.logical   :refer [nor]]
              [mid-fruits.loop      :refer [reduce-indexed]]
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
  [extension-id _]
  [elements/button :item-lister/quit-search-mode-button
                   {:on-click [:item-lister/toggle-search-mode! extension-id]
                    :preset   :close-icon-button
                    :keypress {:key-code 27}}])

(defn search-items-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [elements/search-field :item-lister/search-items-field
                         {:auto-focus?   true
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
  ;
  ; @return (component)
  [extension-id _]
  [elements/button :item-lister/toggle-search-mode-button
                   {:on-click [:item-lister/toggle-search-mode! extension-id]
                    :preset   :search-icon-button
                    :tooltip  :search}])

(defn search-block
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-lister/search-block :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [% @(a/subscribe [:item-lister/get-menu-mode-props extension-id item-namespace])]
       (if (:viewport-small? %) [toggle-search-mode-button extension-id item-namespace]
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
  [extension-id _]
  [elements/button :item-lister/quit-select-mode-button
                   {:on-click [:item-lister/toggle-select-mode! extension-id]
                    :preset   :close-icon-button
                    :keypress {:key-code 27}}])

(defn unselect-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id _]
  [elements/button :item-lister/unselect-all-items-button
                   {:on-click [:item-lister/unselect-all-items! extension-id]
                    :preset   :default-icon-button
                    :icon     :check_box}])

(defn unselect-some-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id _]
  [elements/button :item-lister/unselect-some-items-button
                   {:on-click [:item-lister/unselect-all-items! extension-id]
                    :preset   :default-icon-button
                    :icon     :indeterminate_check_box}])

(defn select-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [elements/button :item-lister/select-all-items-button
                   {:on-click [:item-lister/select-all-items! extension-id item-namespace]
                    :preset   :default-icon-button
                    :icon     :check_box_outline_blank}])

(defn toggle-all-items-selection-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [% @(a/subscribe [:item-lister/get-select-mode-props extension-id item-namespace])]
       (cond (:all-items-selected? %) [unselect-all-items-button  extension-id item-namespace]
             (:any-item-selected?  %) [unselect-some-items-button extension-id item-namespace]
              :no-items-selected      [select-all-items-button    extension-id item-namespace])))

(defn delete-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [% @(a/subscribe [:item-lister/get-select-mode-props extension-id item-namespace])]
       (if (vector/contains-item? (:item-actions %) :delete)
           [elements/button :item-lister/delete-selected-items-button
                            {:disabled? (:no-items-selected? %)
                             :on-click  [:item-lister/delete-selected-items! extension-id item-namespace]
                             :preset    :delete-icon-button
                             :tooltip   :delete!}])))

(defn duplicate-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [% @(a/subscribe [:item-lister/get-select-mode-props extension-id item-namespace])]
       (if (vector/contains-item? (:item-actions %) :duplicate)
           [elements/button :item-lister/duplicate-selected-items-button
                            {:disabled? (:no-items-selected? %)
                             :on-click  [:item-lister/duplicate-selected-items! extension-id item-namespace]
                             :preset    :duplicate-icon-button
                             :tooltip   :duplicate!}])))



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
  (let [% @(a/subscribe [:item-lister/get-reorder-mode-props extension-id item-namespace])]
       [elements/button :item-lister/save-order-button
                        {:disabled? (-> :order-changed? % not)
                         :on-click  [:item-lister/save-order! extension-id]
                         :label     :save-order!
                         :preset    :primary-button}]))

(defn quit-reorder-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id _]
  [elements/button :item-lister/quit-reorder-mode-button
                   {:on-click [:item-lister/toggle-reorder-mode! extension-id]
                    :preset   :close-icon-button
                    :keypress {:key-code 27}}])



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
  (let [new-item-uri (engine/new-item-uri extension-id item-namespace)]
       [elements/button :item-lister/new-item-button
                        {:on-click [:router/go-to! new-item-uri]
                         :preset   :primary-icon-button
                         :tooltip  :add-new!
                         :icon     :add_circle}]))

(defn new-item-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [% @(a/subscribe [:item-lister/get-menu-mode-props extension-id item-namespace])]
       [elements/select :item-lister/new-item-select
                        {:as-button?      true
                         :autoclear?      true
                         :icon            :add_circle
                         :initial-options (:new-item-options %)
                         :on-select       (engine/add-new-item-event extension-id item-namespace)
                         :preset          :primary-icon-button
                         :tooltip         :add-new!}]))

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
  (let [% @(a/subscribe [:item-lister/get-menu-mode-props extension-id item-namespace])]
       (if (:new-item-options %) [new-item-select extension-id item-namespace]
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
  (let [% @(a/subscribe [:item-lister/get-menu-mode-props extension-id item-namespace])]
       (if (:items-selectable? %) [elements/button :item-lister/toggle-select-mode-button
                                                   {:disabled? (:no-items-to-show? %)
                                                    :on-click  [:item-lister/toggle-select-mode! extension-id]
                                                    :preset    :select-mode-icon-button
                                                    :tooltip   :select}])))

(defn toggle-reorder-mode-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-lister/toggle-reorder-mode-button :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [% @(a/subscribe [:item-lister/get-menu-mode-props extension-id item-namespace])]
       (if (:sortable? %) [elements/button :item-lister/toggle-reorder-mode-button
                                           {:disabled? (:no-items-to-show? %)
                                            :on-click  [:item-lister/toggle-reorder-mode! extension-id]
                                            :preset    :reorder-mode-icon-button
                                            :tooltip   :reorder}])))

(defn sort-items-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-lister/sort-items-button :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [% @(a/subscribe [:item-lister/get-menu-mode-props extension-id item-namespace])]
       [elements/select :item-lister/sort-items-button
                        {:as-button?    true
                         :disabled?     (:no-items-to-show? %)
                         :on-select     [:item-lister/order-items! extension-id item-namespace]
                         :options-label :order-by
                         :preset        :order-by-icon-button
                         :tooltip       :order-by
                         :options-path  [extension-id :item-lister/meta-items :order-by-options]
                         :value-path    [extension-id :item-lister/meta-items :order-by]}]))



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
  [:div.item-lister--header--menu-bar
    [search-items-field      extension-id item-namespace]
    [quit-search-mode-button extension-id item-namespace]])

(defn search-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [% @(a/subscribe [:item-lister/get-search-mode-props extension-id item-namespace])]
       [react-transition/mount-animation {:animation-timeout 500 :mounted? (:search-mode? %)}
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
  (let [% @(a/subscribe [:item-lister/get-select-mode-props extension-id item-namespace])]
       [react-transition/mount-animation {:animation-timeout 500 :mounted? (:select-mode? %)}
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
    [:div.item-lister--header--menu-item-group
      [new-item-block             extension-id item-namespace]
      [sort-items-button          extension-id item-namespace]
      [toggle-select-mode-button  extension-id item-namespace]
      [toggle-reorder-mode-button extension-id item-namespace]]
    [:div.item-lister--header--menu-item-group
      [search-block extension-id item-namespace]]])

(defn menu-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [% @(a/subscribe [:item-lister/get-menu-mode-props extension-id item-namespace])]
       [react-transition/mount-animation {:animation-timeout 500 :mounted? (:menu-mode? %)}
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
    [:div.item-lister--header--menu-item-group
      [elements/button {:layout :icon-button :variant :placeholder}]]
    [:div.item-lister--header--menu-item-group
      [save-order-button extension-id item-namespace]]
    [quit-reorder-mode-button extension-id item-namespace]])

(defn reorder-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [% @(a/subscribe [:item-lister/get-reorder-mode-props extension-id item-namespace])]
       [react-transition/mount-animation {:animation-timeout 500 :mounted? (:reorder-mode? %)}
                                         [reorder-mode-header-structure extension-id item-namespace]]))

(defn header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [{:keys [menu]} @(a/subscribe [:item-lister/get-header-props extension-id item-namespace])]
       [:div#item-lister--header--structure
         (if menu [menu             extension-id item-namespace]
                  [menu-mode-header extension-id item-namespace])
         [reorder-mode-header extension-id item-namespace]
         [select-mode-header  extension-id item-namespace]
         [search-mode-header  extension-id item-namespace]]))

(defn header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:menu (metamorphic-content)(opt)
  ;   :new-item-options (vector)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace header-props]
  [components/stated {:component   [header-structure extension-id item-namespace]
                      :initializer [:db/apply! [extension-id :item-lister/meta-items] merge header-props]}])



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
  ;   és eltűnésekor ne változzon a lista magassága.
  (let [% @(a/subscribe [:item-lister/get-indicator-props extension-id item-namespace])]
       [elements/label {:font-size :xs :color :highlight :font-weight :bold
                        :content (if (or (:downloading-items? %) (nor (:downloading-items? %) (:items-received? %)))
                                     :downloading-items...)}]))

(defn downloading-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [% @(a/subscribe [:item-lister/get-indicator-props extension-id item-namespace])]
       (if-not (and (:all-items-downloaded? %) (:items-received? %)) ; XXX#0499
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
  (let [% @(a/subscribe [:item-lister/get-indicator-props extension-id item-namespace])]
       (if (and (:no-items-to-show? %)
                ; Szükséges a items-received? értékét is vizsgálni, hogy az adatok letöltésének elkezdése
                ; előtti pillanatban ne villanjon fel a no-items-to-show-label felirat!
                (:items-received? %)
                ; Szükséges a downloading-items? értékét is vizsgálni, hogy az adatok letöltése közben
                ; ne jelenjen meg a no-items-to-show-label felirat!
                (-> :downloading-items? % not))
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
  [:<> [no-items-to-show  extension-id item-namespace]
       [downloading-items extension-id item-namespace]])



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
  (let [% @(a/subscribe [:item-lister/get-checkbox-props extension-id item-namespace item-dex])]
       (if (:select-mode? %) [elements/button {:on-click [:item-lister/toggle-item-selection! extension-id item-namespace item-dex]
                                               :preset   (if (:item-selected? %) :checked-icon-button :unchecked-icon-button)}])))

(defn list-item-toggle
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ; @param (map) item
  ;
  ; @return (component)
  [extension-id item-namespace item-dex item]
  (let [{:keys [list-element]} @(a/subscribe [:item-lister/get-body-props extension-id item-namespace])
        on-click               (engine/item-clicked-event       extension-id item-namespace item-dex item)
        on-right-click         (engine/item-right-clicked-event extension-id item-namespace item-dex item)]
       [elements/toggle {:on-click       on-click
                         :on-right-click on-right-click
                         :content [list-element item-dex item]
                         :hover-color :highlight}]))

(defn list-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ; @param (map) item
  ;
  ; @return (component)
  [extension-id item-namespace item-dex item]
  (let [item-disabled? @(a/subscribe [:item-lister/item-disabled? extension-id item-namespace item-dex])]
       [:div.item-lister--list-item--structure
         ; - A lista-elem után (és nem előtt) kirenderelt checkbox elem React-fába
         ;   történő csatolása vagy lecsatolása nem okozza a lista-elem újrarenderelését!
         ; - A {:display :flex :flex-direction :row-reverse} tulajdonságok beállításával a checkbox
         ;   elem a lista-elem előtt jelenik meg.
         [:div.item-lister--list-item {:data-disabled item-disabled?}
                                      [list-item-toggle extension-id item-namespace item-dex item]]
         [list-item-checkbox extension-id item-namespace item-dex]]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selectable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  ; Ha a downloaded-items a get-body-props feliratkozásban lenne, akkor az újonnan letöltött
  ; elemek kirenderelése a meglévő elemek újrarenderelését okozná.
  (let [downloaded-items @(a/subscribe [:item-lister/get-downloaded-items extension-id])]
       (reduce-kv (fn [item-list item-dex {:keys [id] :as item}]
                      (conj item-list
                            ; A lista-elemek React-kulcsának tartalmaznia kell az adott elem indexét,
                            ; hogy a lista-elemek törlésekor a megmaradó elemek alkalmazkodjanak
                            ; az új indexükhöz!
                           ^{:key (str id item-dex)}
                            [list-item-structure extension-id item-namespace item-dex item]))
                  [:div.item-lister--item-list]
                  (param downloaded-items))))

(defn sortable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [:div "sortable"])
  ; Ne renderelődjenek újra a listaelemek, amikor átvált {:reorder-mode? true} állapotra!

(defn item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (hiccup)
  [extension-id item-namespace]
  (if false [sortable-item-list   extension-id item-namespace]
            [selectable-item-list extension-id item-namespace]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (hiccup)
  [extension-id item-namespace]
  [:div.item-lister--body--structure
    [item-list             extension-id item-namespace]
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
  ;   :sortable? (boolean)(opt)}
  ;
  ; @usage
  ;  [item-lister/body :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-list-element [item-dex item] [:div ...])
  ;  [item-lister/body :my-extension :my-type {:list-element #'my-list-element}]
  ;
  ; @return (component)
  [extension-id item-namespace body-props]
  [components/stated {:component   [body-structure extension-id item-namespace]
                      :destructor  [:item-lister/unload-lister! extension-id item-namespace]
                      :initializer [:db/apply! [extension-id :item-lister/meta-items] merge body-props]}])



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
  (let [% @(a/subscribe [:item-lister/get-view-props extension-id item-namespace])]
       [layouts/layout-a extension-id {:body   [body   extension-id item-namespace view-props]
                                       :header [header extension-id item-namespace view-props]
                                       :description (:description %)}]))

(defn view
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) view-props
  ;  {:list-element (metamorphic-content)
  ;   :menu (metamorphic-content)(opt)
  ;   :new-item-options (vector)(opt)
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :sortable? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [item-lister/view :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-list-element [item-dex item] [:div ...])
  ;  (defn my-menu         [extension-id item-namespace header-props] [:div ...])
  ;  [item-lister/view :my-extension :my-type {:list-element #'my-list-element
  ;                                            :menu         #'my-menu
  ;                                            :new-item-options [:add-my-type! :add-your-type!]}]
  ;
  ; @return (component)
  [extension-id item-namespace view-props]
  [view-structure extension-id item-namespace view-props])
