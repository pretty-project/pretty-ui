
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.body.views
    (:require [mid-fruits.logical                  :refer [nor]]
              [plugins.item-lister.core.helpers    :as core.helpers]
              [plugins.item-lister.core.prototypes :as core.prototypes]
              [reagent.api                         :as reagent]
              [x.app-core.api                      :as a]
              [x.app-elements.api                  :as elements]
              [x.app-tools.api                     :as tools]

              ; TEMP
              [plugins.sortable.core              :refer []]))



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
                                         ;:color     (if item-selected? :primary :default)

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
  [:<> [elements/horizontal-separator {:size :xxl}]
       [elements/label {:min-height :m :content :an-error-occured :font-size :m       :color :warning}]
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
