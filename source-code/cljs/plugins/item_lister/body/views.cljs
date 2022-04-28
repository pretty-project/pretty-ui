
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.body.views
    (:require [mid-fruits.logical                  :refer [nor]]
              [plugins.item-lister.body.prototypes :as body.prototypes]
              [plugins.item-lister.core.helpers    :as core.helpers]
              [plugins.plugin-handler.body.views   :as body.views]
              [reagent.api                         :as reagent]
              [x.app-core.api                      :as a]
              [x.app-elements.api                  :as elements]
              [x.app-tools.api                     :as tools]

              ; TEMP
              [plugins.sortable.core               :refer []]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.body.views
(def error-body body.views/error-body)



;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn downloading-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  ; Az adatok letöltésének megkezdése előtti pillanatban is szükséges megjeleníteni
  ; a downloading-items komponenst, hogy ne a body komponens megjelenése után
  ; villanjon fel!
  (let [all-items-downloaded? @(a/subscribe [:item-lister/all-items-downloaded? lister-id])
        data-received?        @(a/subscribe [:item-lister/data-received?        lister-id])]
       ; TEMP
       (if-not (and all-items-downloaded? data-received?) ; XXX#0499
               [:div {:style {:width "100%" :grid-row-gap "24px" :display "flex" :flex-direction "column" :padding "24px"}}
                     [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px"}}]
                     [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px"}}]
                     [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px"}}]
                     [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px"}}]
                     [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px"}}]])))
       ; TEMP

(defn no-items-to-show-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [downloading-items? @(a/subscribe [:item-lister/downloading-items? lister-id])
        data-received?     @(a/subscribe [:item-lister/data-received?     lister-id])
        no-items-to-show?  @(a/subscribe [:item-lister/no-items-to-show?  lister-id])]
       (if (and no-items-to-show? data-received?
                ; - Szükséges a data-received? értékét is vizsgálni, hogy az adatok letöltésének elkezdése
                ;   előtti pillanatban ne villanjon fel a no-items-to-show-label felirat!
                ;
                ; - Szükséges a downloading-items? értékét is vizsgálni, hogy az adatok letöltése közben
                ;   ne jelenjen meg a no-items-to-show-label felirat!
                (not downloading-items?))
           [elements/label ::no-items-to-show-label
                           {:color       :highlight
                            :content     :no-items-to-show
                            :font-size   :xs
                            :font-weight :bold
                            :indent      {:all :xs}}])))

(defn no-items-to-show
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [elements/row ::no-items-to-show
                {:content [no-items-to-show-label lister-id]
                 :horizontal-align :center}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  ; A lista-elemek React-kulcsának tartalmaznia kell az adott elem indexét, hogy a lista-elemek
  ; törlésekor a megmaradó elemek alkalmazkodjanak az új indexükhöz!
  (let [downloaded-items @(a/subscribe [:item-lister/get-downloaded-items lister-id])
        list-element     @(a/subscribe [:item-lister/get-body-prop        lister-id :list-element])]
       (letfn [(f [item-list item-dex {:keys [id] :as item}]
                  (conj item-list ^{:key (str id item-dex)}
                                   [list-element lister-id item-dex item]))]
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
         [error-body lister-id {:error-description :the-content-you-opened-may-be-broken}]
        ;@(a/subscribe [:environment/browser-offline?])
        ; [offline-body lister-id]
        @(a/subscribe [:item-lister/data-received? lister-id])
         [:div.item-lister--body--structure [item-list             lister-id]
                                            [tools/infinite-loader lister-id {:on-viewport [:item-lister/request-items! lister-id]}]
                                            [no-items-to-show      lister-id]
                                            [downloading-items     lister-id]]
        :data-not-received
         [downloading-items lister-id]))

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
  ;   :select-mode? (boolean)(opt)
  ;    Default: false
  ;   :selected-items (strings in vector)(opt)
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
  (let [body-props (body.prototypes/body-props-prototype lister-id body-props)]
       (reagent/lifecycles (core.helpers/component-id lister-id :body)
                           {:reagent-render         (fn []              [body-structure                 lister-id])
                            :component-did-mount    (fn []  (a/dispatch [:item-lister/body-did-mount    lister-id body-props]))
                            :component-will-unmount (fn []  (a/dispatch [:item-lister/body-will-unmount lister-id]))
                            :component-did-update   (fn [%] (let [[_ body-props] (reagent/arguments %)]
                                                                 (a/dispatch [:item-lister/body-did-update lister-id body-props])))})))
