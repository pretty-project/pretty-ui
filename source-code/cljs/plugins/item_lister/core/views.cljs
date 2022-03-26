
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.views
    (:require [mid-fruits.logical                  :refer [nor]]
              [mid-fruits.vector                   :as vector]
              [plugins.item-lister.core.helpers    :as core.helpers]
              [plugins.item-lister.core.prototypes :as core.prototypes]
              [react.api                           :as react]
              [reagent.api                         :as reagent]
              [x.app-core.api                      :as a]
              [x.app-elements.api                  :as elements]
              [x.app-tools.api                     :as tools]))
             ;[plugins.item-sorter.api             :refer []]



;; -- Search-mode header components -------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-search-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  [elements/icon-button :item-lister/quit-search-mode-button
                        {:keypress {:key-code 27} :preset :close
                         :on-click [:item-lister/toggle-search-mode! extension-id item-namespace]}])

(defn search-items-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [error-mode?      @(a/subscribe [:item-lister/get-meta-item    extension-id item-namespace :error-mode?])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? extension-id item-namespace])]
       [elements/search-field :item-lister/search-items-field
                              {:auto-focus? true :layout :row :min-width :xs :placeholder :search
                               :disabled?     (or error-mode? lister-disabled?)
                               :on-empty      [:item-lister/search-items! extension-id item-namespace]
                               :on-type-ended [:item-lister/search-items! extension-id item-namespace]
                               :value-path    [:plugins :item-lister/meta-items extension-id :search-term]}]))

(defn toggle-search-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [error-mode?      @(a/subscribe [:item-lister/get-meta-item    extension-id item-namespace :error-mode?])
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
  [extension-id item-namespace]
  [elements/icon-button :item-lister/quit-select-mode-button
                        {:keypress {:key-code 27} :preset :close
                         :on-click [:item-lister/toggle-select-mode! extension-id item-namespace]}])


(defn unselect-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? extension-id item-namespace])]
       [elements/icon-button :item-lister/unselect-all-items-button
                             {:icon :check_box :preset :default :disabled? lister-disabled?
                              :on-click [:item-lister/reset-selections! extension-id item-namespace]}]))

(defn unselect-some-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? extension-id item-namespace])]
       [elements/icon-button :item-lister/unselect-some-items-button
                             {:icon :indeterminate_check_box :preset :default :disabled? lister-disabled?
                              :on-click [:item-lister/reset-selections! extension-id item-namespace]}]))

(defn select-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
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
  [extension-id item-namespace]
  (let [item-actions       @(a/subscribe [:item-lister/get-body-prop      extension-id item-namespace :item-actions])
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
  [extension-id item-namespace]
  (let [item-actions       @(a/subscribe [:item-lister/get-body-prop      extension-id item-namespace :item-actions])
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
  [extension-id item-namespace]
  [elements/icon-button :item-lister/quit-reorder-mode-button
                        {:keypress {:key-code 27} :preset :close
                         :on-click [:item-lister/toggle-reorder-mode! extension-id item-namespace]}])



;; -- Menu-mode header components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-lister/new-item-button :my-extension :my-type]
  [extension-id item-namespace]
  (let [error-mode?      @(a/subscribe [:item-lister/get-meta-item    extension-id item-namespace :error-mode?])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? extension-id item-namespace])
        new-item-event   @(a/subscribe [:item-lister/get-header-prop  extension-id item-namespace :new-item-event])]
       (if-let [new-item-options @(a/subscribe [:item-lister/get-header-prop extension-id item-namespace :new-item-options])]
               [elements/select :item-lister/new-item-select
                                {:as-button? true :autoclear? true :icon :add_circle :preset :primary-icon-button :tooltip :add-new!
                                 :initial-options new-item-options
                                 :on-select       new-item-event
                                 :disabled?       (or error-mode? lister-disabled?)}]
               [elements/icon-button :item-lister/new-item-button
                                     {:icon :add_circle :preset :primary :tooltip :add-new!
                                      :on-click  new-item-event
                                      :disabled? (or error-mode? lister-disabled?)}])))

(defn toggle-select-mode-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-lister/toggle-select-mode-button :my-extension :my-type]
  [extension-id item-namespace]
  (let [error-mode?       @(a/subscribe [:item-lister/get-meta-item     extension-id item-namespace :error-mode?])
        items-selectable? @(a/subscribe [:item-lister/items-selectable? extension-id item-namespace])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  extension-id item-namespace])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? extension-id item-namespace])]
       (if items-selectable? [elements/icon-button :item-lister/toggle-select-mode-button
                                                   {:preset :select-mode :tooltip :select
                                                    :disabled? (or error-mode? lister-disabled? no-items-to-show?)
                                                    :on-click  [:item-lister/toggle-select-mode! extension-id item-namespace]}])))

(defn toggle-reorder-mode-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-lister/toggle-reorder-mode-button :my-extension :my-type]
  [extension-id item-namespace]
  (let [error-mode?       @(a/subscribe [:item-lister/get-meta-item     extension-id item-namespace :error-mode?])
        items-sortable?   @(a/subscribe [:item-lister/get-body-prop     extension-id item-namespace :sortable?])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  extension-id item-namespace])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? extension-id item-namespace])]
       (if items-sortable? [elements/icon-button :item-lister/toggle-reorder-mode-button
                                                 {:preset :reorder-mode :tooltip :reorder
                                                  :disabled? (or error-mode? lister-disabled? no-items-to-show?)
                                                  :on-click  [:item-lister/toggle-reorder-mode! extension-id item-namespace]}])))

(defn sort-items-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-lister/sort-items-button :my-extension :my-type]
  [extension-id item-namespace]
  (let [error-mode?       @(a/subscribe [:item-lister/get-meta-item     extension-id item-namespace :error-mode?])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  extension-id item-namespace])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? extension-id item-namespace])]
       [elements/select :item-lister/sort-items-button
                        {:as-button? true :options-label :order-by :preset :order-by-icon-button :tooltip :order-by
                         :disabled?    (or error-mode? lister-disabled? no-items-to-show?)
                         :on-select    [:item-lister/order-items! extension-id item-namespace]
                         :options-path [:plugins :item-lister/body-props extension-id :order-by-options]
                         :value-path   [:plugins :item-lister/meta-items extension-id :order-by]
                         :get-label-f  core.helpers/order-by-label-f}]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn search-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  [:div.item-lister--header--menu-bar [search-items-field      extension-id item-namespace]
                                      [quit-search-mode-button extension-id item-namespace]])

(defn search-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [search-mode? @(a/subscribe [:item-lister/get-meta-item extension-id item-namespace :search-mode?])]
       [react/mount-animation {:animation-timeout 500 :mounted? search-mode?}
                              [search-mode-header-structure extension-id item-namespace]]))

(defn select-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
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
  [extension-id item-namespace]
  (let [select-mode? @(a/subscribe [:item-lister/get-meta-item extension-id item-namespace :select-mode?])]
       [react/mount-animation {:animation-timeout 500 :mounted? select-mode?}
                              [select-mode-header-structure extension-id item-namespace]]))

(defn menu-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group [new-item-button            extension-id item-namespace]
                                               [sort-items-button          extension-id item-namespace]
                                               [toggle-select-mode-button  extension-id item-namespace]
                                               [toggle-reorder-mode-button extension-id item-namespace]]
    [:div.item-lister--header--menu-item-group [search-block extension-id item-namespace]]])

(defn menu-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [reorder-mode? @(a/subscribe [:item-lister/get-meta-item extension-id item-namespace :reorder-mode?])
        search-mode?  @(a/subscribe [:item-lister/get-meta-item extension-id item-namespace :search-mode?])]
       [react/mount-animation {:animation-timeout 500 :mounted? (nor reorder-mode? search-mode?)}
                              (if-let [menu-element @(a/subscribe [:item-lister/get-header-prop extension-id item-namespace :menu-element])]
                                      [menu-element               extension-id item-namespace]
                                      [menu-mode-header-structure extension-id item-namespace])]))
(defn reorder-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
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
  [extension-id item-namespace]
  (let [reorder-mode? @(a/subscribe [:item-lister/get-meta-item extension-id item-namespace :reorder-mode?])]
       [react/mount-animation {:animation-timeout 500 :mounted? reorder-mode?}
                              [reorder-mode-header-structure extension-id item-namespace]]))

(defn header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (if @(a/subscribe [:item-lister/header-did-mount? extension-id item-namespace])
       [:div#item-lister--header--structure [menu-mode-header    extension-id item-namespace]
                                            [reorder-mode-header extension-id item-namespace]
                                            [select-mode-header  extension-id item-namespace]
                                            [search-mode-header  extension-id item-namespace]]))

(defn header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:menu-element (metamorphic-content)(opt)
  ;   :new-item-event (metamorphic-event)(opt)
  ;   :new-item-options (vector)(opt)}
  ;
  ; @usage
  ;  [item-lister/header :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-menu-element [extension-id item-namespace] [:div ...])
  ;  [item-lister/header :my-extension :my-type {:menu #'my-menu-element}}]
  [extension-id item-namespace header-props]
  (reagent/lifecycles (core.helpers/component-id extension-id item-namespace :header)
                      {:reagent-render         (fn []             [header-structure                 extension-id item-namespace])
                       :component-did-mount    (fn [] (a/dispatch [:item-lister/header-did-mount    extension-id item-namespace header-props]))
                       :component-will-unmount (fn [] (a/dispatch [:item-lister/header-will-unmount extension-id item-namespace]))}))



;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn downloading-items-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  ; - Az adatok letöltésének megkezdése előtti pillanatban is szükséges megjeleníteni
  ;   a downloading-items-label feliratot
  ; - Ha még nincs letöltve az összes elem és várható a downloading-items-label felirat megjelenése,
  ;   addig tartalom nélküli placeholder elemként biztosítja, hogy a felirat megjelenésekor
  ;   és eltűnésekor ne változzon a lista magassága
  (let [all-items-downloaded? @(a/subscribe [:item-lister/all-items-downloaded? extension-id item-namespace])
        downloading-items?    @(a/subscribe [:item-lister/downloading-items? extension-id item-namespace])
        items-received?       @(a/subscribe [:item-lister/items-received?       extension-id item-namespace])]
       (if-not (and all-items-downloaded? items-received?) ; XXX#0499
               [elements/label {:font-size :xs :color :highlight :font-weight :bold
                                :content (if (or downloading-items? (nor downloading-items? items-received?))
                                             :downloading-items...)}])))

(defn no-items-to-show-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
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
           [elements/label {:content :no-items-to-show :font-size :xs :color :highlight :font-weight :bold}])))

(defn indicators
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  [elements/column {:content [:<> [no-items-to-show-label  extension-id item-namespace]
                                  [downloading-items-label extension-id item-namespace]]}])



;; -- List-item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-item-checkbox
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  [extension-id item-namespace item-dex]
  (let [item-selected?   @(a/subscribe [:item-lister/item-selected?   extension-id item-namespace item-dex])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? extension-id item-namespace])
        select-mode?     @(a/subscribe [:item-lister/get-meta-item    extension-id item-namespace :select-mode?])]
       (if select-mode? [elements/button {:disabled? lister-disabled?
                                          :on-click  [:item-lister/toggle-item-selection! extension-id item-namespace item-dex]
                                          :preset    (if item-selected? :checked-icon-button :unchecked-icon-button)}])))

(defn list-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ; @param (map) item
  [extension-id item-namespace item-dex item]
  (let [item-disabled? @(a/subscribe [:item-lister/item-disabled? extension-id item-namespace item-dex])
        list-element   @(a/subscribe [:item-lister/get-body-prop  extension-id item-namespace :list-element])]
       [:div.item-lister--list-item--structure
         ; - A lista-elem után (és nem előtt) kirenderelt checkbox elem React-fába
         ;   történő csatolása vagy lecsatolása nem okozza a lista-elem újrarenderelését!
         ; - A {:display :flex :flex-direction :row-reverse} tulajdonságok beállításával a checkbox
         ;   elem a lista-elem előtt jelenik meg.
         [:div.item-lister--list-item {:data-disabled item-disabled?}
                                      [list-element extension-id item-namespace item-dex item]]
         [list-item-checkbox extension-id item-namespace item-dex]]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [_ _]
  [:<> [elements/label {:min-height :m :content :an-error-occured :font-size :m}]
       [elements/label {:min-height :m :content :the-content-you-opened-may-be-broken :color :muted}]])

(defn offline-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [_ _])

(defn selectable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [downloaded-items @(a/subscribe [:item-lister/get-downloaded-items extension-id item-namespace])]
       (letfn [(f [item-list item-dex {:keys [id] :as item}]
                  (conj item-list
                        ; A lista-elemek React-kulcsának tartalmaznia kell az adott elem indexét,
                        ; hogy a lista-elemek törlésekor a megmaradó elemek alkalmazkodjanak
                        ; az új indexükhöz!
                       ^{:key (str id item-dex)}
                        [list-item-structure extension-id item-namespace item-dex item]))]
              (reduce-kv f [:div.item-lister--item-list] downloaded-items))))

(defn sortable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  [:div "sortable"])
  ; Ne renderelődjenek újra a listaelemek, amikor átvált {:reorder-mode? true} állapotra!

(defn item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (if false [sortable-item-list   extension-id item-namespace]
            [selectable-item-list extension-id item-namespace]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (cond @(a/subscribe [:item-lister/get-meta-item extension-id item-namespace :error-mode?])
         [error-body extension-id item-namespace]
        ;@(a/subscribe [:environment/browser-offline?])
        ; [offline-body extension-id item-namespace]
        @(a/subscribe [:item-lister/body-did-mount? extension-id item-namespace])
         [:div.item-lister--body--structure
           [item-list             extension-id item-namespace]
           [tools/infinite-loader extension-id {:on-viewport [:item-lister/request-items! extension-id item-namespace]}]
           [indicators            extension-id item-namespace]]))

(defn body
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:download-limit (integer)(opt)
  ;    Default: core.config/DEFAULT-DOWNLOAD-LIMIT
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :items-path (vector)(opt)
  ;    Default: core.helpers/default-items-path
  ;   :list-element (metamorphic-content)
  ;   :order-by-options (namespaced keywords in vector)(opt)
  ;    Default: core.config/DEFAULT-ORDER-BY-OPTIONS
  ;   :prefilter (map)(opt)
  ;   :search-keys (keywords in vector)(opt)
  ;    Default: core.config/DEFAULT-SEARCH-KEYS
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
  [extension-id item-namespace body-props]
  (let [body-props (core.prototypes/body-props-prototype extension-id item-namespace body-props)]
       (reagent/lifecycles (core.helpers/component-id extension-id item-namespace :body)
                           {:reagent-render         (fn []             [body-structure                 extension-id item-namespace])
                            :component-did-mount    (fn [] (a/dispatch [:item-lister/body-did-mount    extension-id item-namespace body-props]))
                            :component-will-unmount (fn [] (a/dispatch [:item-lister/body-will-unmount extension-id item-namespace]))})))
