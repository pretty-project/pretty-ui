
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
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button :item-lister/quit-search-mode-button
                             {:keypress  {:key-code 27} :preset :close
                              :disabled? lister-disabled?
                              :on-click  [:item-lister/toggle-search-mode! lister-id]}]))

(defn search-items-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [error-mode?      @(a/subscribe [:item-lister/get-meta-item    lister-id :error-mode?])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/search-field :item-lister/search-items-field
                              {:auto-focus? true :layout :row :min-width :xs :placeholder :search
                               :disabled?     (or error-mode? lister-disabled?)
                               :on-empty      [:item-lister/search-items!          lister-id]
                               :on-type-ended [:item-lister/search-items!          lister-id]
                               :value-path    [:plugins :plugin-handler/meta-items lister-id :search-term]}]))

(defn toggle-search-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [error-mode?      @(a/subscribe [:item-lister/get-meta-item    lister-id :error-mode?])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button :item-lister/toggle-search-mode-button
                             {:preset :search :tooltip :search
                              :disabled? (or error-mode? lister-disabled?)
                              :on-click  [:item-lister/toggle-search-mode! lister-id]}]))

(defn search-block
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [item-lister/search-block :my-lister]
  [lister-id]
  (let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
       (if viewport-small? [toggle-search-mode-button lister-id]
                           [search-items-field        lister-id])))



;; -- Select-mode header components -------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-select-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button :item-lister/quit-select-mode-button
                             {:keypress  {:key-code 27} :preset :close
                              :disabled? lister-disabled?
                              :on-click  [:item-lister/toggle-select-mode! lister-id]}]))


(defn unselect-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button :item-lister/unselect-all-items-button
                             {:icon :check_box :preset :default :disabled? lister-disabled?
                              :on-click [:item-lister/reset-selections! lister-id]}]))

(defn unselect-some-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button :item-lister/unselect-some-items-button
                             {:icon :indeterminate_check_box :preset :default :disabled? lister-disabled?
                              :on-click [:item-lister/reset-selections! lister-id]}]))

(defn select-all-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button :item-lister/select-all-items-button
                             {:icon :check_box_outline_blank :preset :default :disabled? lister-disabled?
                              :on-click [:item-lister/select-all-items! lister-id]}]))

(defn toggle-all-items-selection-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [all-items-selected? @(a/subscribe [:item-lister/all-items-selected? lister-id])
        any-item-selected?  @(a/subscribe [:item-lister/any-item-selected?  lister-id])]
       (cond all-items-selected? [unselect-all-items-button  lister-id]
             any-item-selected?  [unselect-some-items-button lister-id]
             :no-items-selected  [select-all-items-button    lister-id])))

(defn delete-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [item-actions       @(a/subscribe [:item-lister/get-body-prop      lister-id :item-actions])
        lister-disabled?   @(a/subscribe [:item-lister/lister-disabled?   lister-id])
        no-items-selected? @(a/subscribe [:item-lister/no-items-selected? lister-id])]
       (if (vector/contains-item? item-actions :delete)
           [elements/icon-button :item-lister/delete-selected-items-button
                                 {:preset :delete :tooltip :delete!
                                  :disabled? (or lister-disabled? no-items-selected?)
                                  :on-click  [:item-lister/delete-selected-items! lister-id]}])))

(defn duplicate-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [item-actions       @(a/subscribe [:item-lister/get-body-prop      lister-id :item-actions])
        lister-disabled?   @(a/subscribe [:item-lister/lister-disabled?   lister-id])
        no-items-selected? @(a/subscribe [:item-lister/no-items-selected? lister-id])]
       (if (vector/contains-item? item-actions :duplicate)
           [elements/icon-button :item-lister/duplicate-selected-items-button
                                 {:preset :duplicate :tooltip :duplicate!
                                  :disabled? (or lister-disabled? no-items-selected?)
                                  :on-click  [:item-lister/duplicate-selected-items! lister-id]}])))



;; -- Reorder-mode header components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-reorder-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
       [elements/icon-button :item-lister/quit-reorder-mode-button
                             {:keypress  {:key-code 27} :preset :close
                              :disabled? lister-disabled?
                              :on-click  [:item-lister/toggle-reorder-mode! lister-id]}]))
(defn save-order-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])
        order-changed?   @(a/subscribe [:item-lister/order-changed?   lister-id])]
       [elements/button :item-lister/save-order-button
                        {:label :save-order! :preset :primary-button
                         :disabled? (or lister-disabled? (not order-changed?))
                         :on-click  [:item-lister/save-order! lister-id]}]))



;; -- Menu-mode header components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item-button
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [item-lister/new-item-button :my-lister]
  [lister-id]
  (if-let [new-item-event @(a/subscribe [:item-lister/get-header-prop lister-id :new-item-event])]
          (let [error-mode?      @(a/subscribe [:item-lister/get-meta-item    lister-id :error-mode?])
                lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])]
               (if-let [new-item-options @(a/subscribe [:item-lister/get-header-prop lister-id :new-item-options])]
                       [elements/select :item-lister/new-item-select
                                        {:as-button? true :autoclear? true :icon :add_circle :preset :primary-icon-button :tooltip :add-new!
                                         :initial-options new-item-options
                                         :on-select       new-item-event
                                         :disabled?       (or error-mode? lister-disabled?)}]
                       [elements/icon-button :item-lister/new-item-button
                                             {:icon :add_circle :preset :primary :tooltip :add-new!
                                              :on-click  new-item-event
                                              :disabled? (or error-mode? lister-disabled?)}]))))


(defn toggle-select-mode-button
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [item-lister/toggle-select-mode-button :my-lister]
  [lister-id]
  (let [error-mode?       @(a/subscribe [:item-lister/get-meta-item     lister-id :error-mode?])
        items-selectable? @(a/subscribe [:item-lister/items-selectable? lister-id])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  lister-id])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? lister-id])]
       (if items-selectable? [elements/icon-button :item-lister/toggle-select-mode-button
                                                   {:preset :select-mode :tooltip :select
                                                    :disabled? (or error-mode? lister-disabled? no-items-to-show?)
                                                    :on-click  [:item-lister/toggle-select-mode! lister-id]}])))

(defn toggle-reorder-mode-button
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [item-lister/toggle-reorder-mode-button :my-lister]
  [lister-id]
  (let [error-mode?       @(a/subscribe [:item-lister/get-meta-item     lister-id :error-mode?])
        items-sortable?   @(a/subscribe [:item-lister/get-body-prop     lister-id :sortable?])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  lister-id])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? lister-id])]
       (if items-sortable? [elements/icon-button :item-lister/toggle-reorder-mode-button
                                                 {:preset :reorder-mode :tooltip :reorder
                                                  :disabled? (or error-mode? lister-disabled? no-items-to-show?)
                                                  :on-click  [:item-lister/toggle-reorder-mode! lister-id]}])))

(defn sort-items-button
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  [item-lister/sort-items-button :my-lister]
  [lister-id]
  (let [error-mode?       @(a/subscribe [:item-lister/get-meta-item     lister-id :error-mode?])
        lister-disabled?  @(a/subscribe [:item-lister/lister-disabled?  lister-id])
        no-items-to-show? @(a/subscribe [:item-lister/no-items-to-show? lister-id])]
       [elements/select :item-lister/sort-items-button
                        {:as-button? true :options-label :order-by :preset :order-by-icon-button :tooltip :order-by
                         :disabled?    (or error-mode? lister-disabled? no-items-to-show?)
                         :on-select    [:item-lister/order-items!           lister-id]
                         :options-path [:plugins :plugin-handler/body-props lister-id :order-by-options]
                         :value-path   [:plugins :plugin-handler/meta-items lister-id :order-by]
                         :get-label-f  core.helpers/order-by-label-f}]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn search-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [:div.item-lister--header--menu-bar [search-items-field      lister-id]
                                      [quit-search-mode-button lister-id]])

(defn search-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [search-mode? @(a/subscribe [:item-lister/get-meta-item lister-id :search-mode?])]
       [react/mount-animation {:animation-timeout 500 :mounted? search-mode?}
                              [search-mode-header-structure lister-id]]))

(defn select-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group
      [toggle-all-items-selection-button lister-id]
      [delete-selected-items-button      lister-id]
      [duplicate-selected-items-button   lister-id]]
    [quit-select-mode-button lister-id]])

(defn select-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [select-mode? @(a/subscribe [:item-lister/get-meta-item lister-id :select-mode?])]
       [react/mount-animation {:animation-timeout 500 :mounted? select-mode?}
                              [select-mode-header-structure lister-id]]))

(defn menu-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group [new-item-button            lister-id]
                                               [sort-items-button          lister-id]
                                               [toggle-select-mode-button  lister-id]
                                               [toggle-reorder-mode-button lister-id]]
    [:div.item-lister--header--menu-item-group [search-block lister-id]]])

(defn menu-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [reorder-mode? @(a/subscribe [:item-lister/get-meta-item lister-id :reorder-mode?])
        search-mode?  @(a/subscribe [:item-lister/get-meta-item lister-id :search-mode?])]
       [react/mount-animation {:animation-timeout 500 :mounted? (nor reorder-mode? search-mode?)}
                              (if-let [menu-element @(a/subscribe [:item-lister/get-header-prop lister-id :menu-element])]
                                      [menu-element               lister-id]
                                      [menu-mode-header-structure lister-id])]))
(defn reorder-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group [elements/button {:layout :icon-button :variant :placeholder}]]
    [:div.item-lister--header--menu-item-group [save-order-button lister-id]]
    [quit-reorder-mode-button lister-id]])

(defn reorder-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [reorder-mode? @(a/subscribe [:item-lister/get-meta-item lister-id :reorder-mode?])]
       [react/mount-animation {:animation-timeout 500 :mounted? reorder-mode?}
                              [reorder-mode-header-structure lister-id]]))

(defn header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (if @(a/subscribe [:item-lister/header-did-mount? lister-id])
       [:div#item-lister--header--structure [menu-mode-header    lister-id]
                                            [reorder-mode-header lister-id]
                                            [select-mode-header  lister-id]
                                            [search-mode-header  lister-id]]))

(defn header
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ;  {:menu-element (metamorphic-content)(opt)
  ;   :new-item-event (metamorphic-event)(opt)
  ;   :new-item-options (vector)(opt)}
  ;
  ; @usage
  ;  [item-lister/header :my-lister {...}]
  ;
  ; @usage
  ;  (defn my-menu-element [lister-id] [:div ...])
  ;  [item-lister/header :my-lister {:menu #'my-menu-element}}]
  [lister-id header-props]
  (reagent/lifecycles (core.helpers/component-id lister-id :header)
                      {:reagent-render         (fn []             [header-structure                 lister-id])
                       :component-did-mount    (fn [] (a/dispatch [:item-lister/header-did-mount    lister-id header-props]))
                       :component-will-unmount (fn [] (a/dispatch [:item-lister/header-will-unmount lister-id]))}))



;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn downloading-items-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  ; - Az adatok letöltésének megkezdése előtti pillanatban is szükséges megjeleníteni
  ;   a downloading-items-label feliratot
  ; - Ha még nincs letöltve az összes elem és várható a downloading-items-label felirat megjelenése,
  ;   addig tartalom nélküli placeholder elemként biztosítja, hogy a felirat megjelenésekor
  ;   és eltűnésekor ne változzon a lista magassága
  (let [all-items-downloaded? @(a/subscribe [:item-lister/all-items-downloaded? lister-id])
        downloading-items?    @(a/subscribe [:item-lister/downloading-items?    lister-id])
        items-received?       @(a/subscribe [:item-lister/items-received?       lister-id])]
       (if-not (and all-items-downloaded? items-received?) ; XXX#0499
               [elements/label {:font-size :xs :color :highlight :font-weight :bold
                                :content (if (or downloading-items? (nor downloading-items? items-received?))
                                             :downloading-items...)}])))

(defn no-items-to-show-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [downloading-items? @(a/subscribe [:item-lister/downloading-items? lister-id])
        items-received?    @(a/subscribe [:item-lister/items-received?    lister-id])
        no-items-to-show?  @(a/subscribe [:item-lister/no-items-to-show?  lister-id])]
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
  ; @param (keyword) lister-id
  [lister-id]
  ; A column komponens {:stretch-orientation :vertical} alapbeállítással használva popup elemen
  ; megejelenített item-lister esetén a {height: 100%} css tulajdonság miatt örökölte a szülő
  ; elem teljes magasságát.
  [elements/column {:content [:<> [no-items-to-show-label  lister-id]
                                  [downloading-items-label lister-id]]
                    :stretch-orientation :none}])



;; -- List-item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-item-checkbox
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  [lister-id item-dex]
  (let [item-selected?   @(a/subscribe [:item-lister/item-selected?   lister-id item-dex])
        lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])
        select-mode?     @(a/subscribe [:item-lister/get-meta-item    lister-id :select-mode?])]
       (if select-mode? [elements/button {:disabled? lister-disabled?
                                          :on-click  [:item-lister/toggle-item-selection! lister-id item-dex]
                                          :preset    (if item-selected? :checked-icon-button :unchecked-icon-button)}])))

(defn list-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item
  [lister-id item-dex item]
  (let [item-disabled? @(a/subscribe [:item-lister/item-disabled? lister-id item-dex])
        list-element   @(a/subscribe [:item-lister/get-body-prop  lister-id :list-element])]
       [:div.item-lister--list-item--structure
         ; - A lista-elem után (és nem előtt) kirenderelt checkbox elem React-fába
         ;   történő csatolása vagy lecsatolása nem okozza a lista-elem újrarenderelését!
         ; - A {:display :flex :flex-direction :row-reverse} tulajdonságok beállításával a checkbox
         ;   elem a lista-elem előtt jelenik meg.
         [:div.item-lister--list-item {:data-disabled item-disabled?}
                                      [list-element lister-id item-dex item]]
         [list-item-checkbox lister-id item-dex]]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [_]
  [:<> [elements/label {:min-height :m :content :an-error-occured :font-size :m}]
       [elements/label {:min-height :m :content :the-content-you-opened-may-be-broken :color :muted}]])

(defn offline-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [_])

(defn selectable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [downloaded-items @(a/subscribe [:item-lister/get-downloaded-items lister-id])]
       (letfn [(f [item-list item-dex {:keys [id] :as item}]
                  (conj item-list
                        ; A lista-elemek React-kulcsának tartalmaznia kell az adott elem indexét,
                        ; hogy a lista-elemek törlésekor a megmaradó elemek alkalmazkodjanak
                        ; az új indexükhöz!
                       ^{:key (str id item-dex)}
                        [list-item-structure lister-id item-dex item]))]
              (reduce-kv f [:div.item-lister--item-list] downloaded-items))))

(defn sortable-item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [:div "sortable"])
  ; Ne renderelődjenek újra a listaelemek, amikor átvált {:reorder-mode? true} állapotra!

(defn item-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (if false [sortable-item-list   lister-id]
            [selectable-item-list lister-id]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (cond @(a/subscribe [:item-lister/get-meta-item lister-id :error-mode?])
         [error-body lister-id]
        ;@(a/subscribe [:environment/browser-offline?])
        ; [offline-body lister-id]
        @(a/subscribe [:item-lister/body-did-mount? lister-id])
         [:div.item-lister--body--structure [item-list             lister-id]
                                            [tools/infinite-loader lister-id {:on-viewport [:item-lister/request-items! lister-id]}]
                                            [indicators            lister-id]]))

(defn body
  ; @param (keyword) lister-id
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
  ;  [item-lister/body :my-lister {...}]
  ;
  ; @usage
  ;  (defn my-list-element [lister-id item-dex item] [:div ...])
  ;  [item-lister/body :my-lister {:list-element #'my-list-element
  ;                                :prefilter    {:my-type/color "red"}}]
  [lister-id body-props]
  (let [body-props (core.prototypes/body-props-prototype lister-id body-props)]
       (reagent/lifecycles (core.helpers/component-id lister-id :body)
                           {:reagent-render         (fn []             [body-structure                 lister-id])
                            :component-did-mount    (fn [] (a/dispatch [:item-lister/body-did-mount    lister-id body-props]))
                            :component-will-unmount (fn [] (a/dispatch [:item-lister/body-will-unmount lister-id]))})))
