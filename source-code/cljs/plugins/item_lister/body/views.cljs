
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.body.views
    (:require [mid-fruits.logical                  :refer [nor]]
              [plugins.item-lister.body.prototypes :as body.prototypes]
              [plugins.item-lister.core.helpers    :as core.helpers]
              [plugins.plugin-handler.body.views   :as body.views]
              [reagent.api                         :as reagent]
              [x.app-components.api                :as components]
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
  (if-let [ghost-element @(a/subscribe [:item-lister/get-body-prop lister-id :ghost-element])]
          (let [all-items-downloaded? @(a/subscribe [:item-lister/all-items-downloaded? lister-id])
                data-received?        @(a/subscribe [:item-lister/data-received?        lister-id])]
               (if-not (and all-items-downloaded? data-received?)
                       [components/content ghost-element]))))

(defn no-items-to-show-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  ; - Szükséges a data-received? értékét is vizsgálni, hogy az adatok letöltésének elkezdése
  ;   előtti pillanatban ne villanjon fel a no-items-to-show-label felirat!
  ;
  ; - Szükséges a downloading-items? értékét is vizsgálni, hogy az adatok letöltése közben
  ;   ne jelenjen meg a no-items-to-show-label felirat!
  (let [downloading-items? @(a/subscribe [:item-lister/downloading-items? lister-id])
        data-received?     @(a/subscribe [:item-lister/data-received?     lister-id])
        no-items-to-show?  @(a/subscribe [:item-lister/no-items-to-show?  lister-id])]
       (if (and no-items-to-show? data-received? (not downloading-items?))
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

(defn item-list
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

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (cond @(a/subscribe [:item-lister/get-meta-item lister-id :error-mode?])
         [error-body lister-id {:error-description :the-content-you-opened-may-be-broken}]
        @(a/subscribe [:item-lister/data-received? lister-id])
         [:<> [item-list             lister-id]
              [tools/infinite-loader lister-id {:on-viewport [:item-lister/request-items! lister-id]}]
              [no-items-to-show      lister-id]
              [downloading-items     lister-id]]
        :data-not-received
         [downloading-items lister-id]))

(defn body
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ;  {:default-order-by (namespaced keyword)
  ;   :download-limit (integer)(opt)
  ;    Default: core.config/DEFAULT-DOWNLOAD-LIMIT
  ;   :ghost-element (metamorphic-content)(opt)
  ;   :items-path (vector)(opt)
  ;    Default: core.helpers/default-items-path
  ;   :list-element (metamorphic-content)

  ;   WARNING! DEPRECATED! DO NOT USE!
  ;   :order-by-options (namespaced keywords in vector)(opt)
  ;    Default: core.config/DEFAULT-ORDER-BY-OPTIONS

  ;   :prefilter (map)(opt)
  ;   :query (vector)(opt)
  ;   :selected-items (strings in vector)(opt)}
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
                            :component-did-update   (fn [%] (a/dispatch [:item-lister/body-did-update   lister-id %]))})))
