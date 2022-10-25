
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
              [reagent.api                         :as reagent]
              [re-frame.api                        :as r]
              [tools.infinite-loader.api           :as infinite-loader]
              [x.app-components.api                :as components]
              [x.app-elements.api                  :as elements]))



;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn placeholder-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  ; - Szükséges a data-received? értékét is vizsgálni, hogy az adatok letöltésének elkezdése
  ;   előtti pillanatban ne villanjon fel a no-items-to-show-label felirat!
  ;
  ; - Szükséges a downloading-items? értékét is vizsgálni, hogy az adatok letöltése közben
  ;   ne jelenjen meg a no-items-to-show-label felirat!
  (let [downloading-items? @(r/subscribe [:item-lister/downloading-items? lister-id])
        data-received?     @(r/subscribe [:item-lister/data-received?     lister-id])
        no-items-to-show?  @(r/subscribe [:item-lister/no-items-to-show?  lister-id])
        placeholder        @(r/subscribe [:item-lister/get-body-prop      lister-id :placeholder])]
       (if (and no-items-to-show? data-received? (not downloading-items?))
           [elements/label ::placeholder-label
                           {:color       :highlight
                            :content     placeholder
                            :font-size   :xs
                            :font-weight :bold
                            :indent      {:all :xs}}])))

(defn placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [elements/row ::placeholder
                {:content [placeholder-label lister-id]
                 :horizontal-align :center}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  ; Az adatok letöltésének megkezdése előtti pillanatban is szükséges megjeleníteni
  ; a downloading-items komponenst, hogy ne a body komponens megjelenése után
  ; villanjon fel!
  (if-let [ghost-element @(r/subscribe [:item-lister/get-body-prop lister-id :ghost-element])]
          (let [all-items-downloaded? @(r/subscribe [:item-lister/all-items-downloaded? lister-id])
                data-received?        @(r/subscribe [:item-lister/data-received?        lister-id])]
               (if-not (and all-items-downloaded? data-received?)
                       [components/content ghost-element]))))

(defn error-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (if-let [error-element @(r/subscribe [:item-lister/get-body-prop lister-id :error-element])]
          [components/content error-element]))

(defn list-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  ; A lista-elemek React-kulcsának tartalmaznia kell az adott elem indexét, hogy a lista-elemek
  ; törlésekor a megmaradó elemek alkalmazkodjanak az új indexükhöz!
  (let [downloaded-items @(r/subscribe [:item-lister/get-downloaded-items lister-id])
        list-element     @(r/subscribe [:item-lister/get-body-prop        lister-id :list-element])]
       [list-element lister-id downloaded-items]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (cond @(r/subscribe [:item-lister/get-meta-item lister-id :error-mode?])
         [error-element lister-id]
        @(r/subscribe [:item-lister/data-received? lister-id])
         [:<> [list-element              lister-id]
              [infinite-loader/component lister-id {:on-viewport [:item-lister/request-items! lister-id]}]
              [placeholder               lister-id]
              [ghost-element             lister-id]]
         :data-not-received
         [ghost-element lister-id]))

(defn body
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ;  {:default-order-by (namespaced keyword)
  ;   :download-limit (integer)(opt)
  ;    Default: core.config/DEFAULT-DOWNLOAD-LIMIT
  ;   :error-element (metamorphic-content)(opt)
  ;   :ghost-element (metamorphic-content)(opt)
  ;   :items-path (vector)(opt)
  ;    Default: core.helpers/default-items-path
  ;   :list-element (metamorphic-content)
  ;   :order-key (keyword)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;    Default: :no-items-to-show
  ;   :prefilter (map)(opt)
  ;   :query (vector)(opt)
  ;   :transfer-id (keyword)(opt)}
  ;
  ; @usage
  ;  [body :my-lister {...}]
  ;
  ; @usage
  ;  (defn my-list-element [lister-id items] [:div ...])
  ;  [body :my-lister {:list-element #'my-list-element
  ;                    :prefilter    {:my-type/color "red"}}]
  [lister-id body-props]
  (let [body-props (body.prototypes/body-props-prototype lister-id body-props)]
       (reagent/lifecycles (core.helpers/component-id lister-id :body)
                           {:component-did-mount    (fn []  (r/dispatch [:item-lister/body-did-mount    lister-id body-props]))
                            :component-will-unmount (fn []  (r/dispatch [:item-lister/body-will-unmount lister-id]))
                            :component-did-update   (fn [%] (r/dispatch [:item-lister/body-did-update   lister-id %]))
                            :reagent-render         (fn []              [body-structure                 lister-id])})))
