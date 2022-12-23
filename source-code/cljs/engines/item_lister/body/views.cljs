
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.body.views
    (:require [elements.api                        :as elements]
              [engines.item-lister.body.prototypes :as body.prototypes]
              [engines.item-lister.core.helpers    :as core.helpers]
              [logic.api                           :refer [nor]]
              [re-frame.api                        :as r]
              [reagent.api                         :as reagent]
              [tools.infinite-loader.api           :as infinite-loader]
              [x.components.api                    :as x.components]))



;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn placeholder-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (let [placeholder @(r/subscribe [:item-lister/get-body-prop lister-id :placeholder])]
       [elements/label ::placeholder-label
                       {:color       :highlight
                        :content     placeholder
                        :font-size   :xs
                        :font-weight :bold
                        :indent      {:all :xs}
                        :line-height :block}]))

(defn placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  ; Szükséges a data-received? értékét is vizsgálni, hogy az adatok letöltésének elkezdése
  ; előtti pillanatban ne villanjon fel a 'placeholder' komponens!
  ;
  ; Szükséges a 'downloading-items?' értékét is vizsgálni, hogy az adatok letöltése közben
  ; ne jelenjen meg a 'placeholder' komponens!
  (let [downloading-items? @(r/subscribe [:item-lister/downloading-items? lister-id])
        data-received?     @(r/subscribe [:item-lister/data-received?     lister-id])
        no-items-to-show?  @(r/subscribe [:item-lister/no-items-to-show?  lister-id])]
       (if (and no-items-to-show? data-received? (not downloading-items?))
           [elements/row ::placeholder
                         {:content [placeholder-label lister-id]
                          :horizontal-align :center}])))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  ; Why {:data-received? false} state causes the rendering of the 'ghost-element' component?
  ; - Before start downloading the data, the 'ghost-element' component has to be shown,
  ;   otherwise it will only shown up after the body component get rendered and it
  ;   would be shown up after a short flicker.
  (if-let [ghost-element @(r/subscribe [:item-lister/get-body-prop lister-id :ghost-element])]
          (let [all-items-downloaded? @(r/subscribe [:item-lister/all-items-downloaded? lister-id])
                data-received?        @(r/subscribe [:item-lister/data-received?        lister-id])]
               (if-not (and all-items-downloaded? data-received?)
                       [x.components/content lister-id ghost-element]))))

(defn error-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  (if-let [error-element @(r/subscribe [:item-lister/get-body-prop lister-id :error-element])]
          [x.components/content lister-id error-element]))

(defn list-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  ; WARNING!
  ; Every list item has to contain its index in its React key!
  ; When a list item removed, an other list item will replace it (in the React tree).
  (let [list-element @(r/subscribe [:item-lister/get-body-prop lister-id :list-element])]
       [x.components/content lister-id list-element]))

(defn infinite-loader
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  [infinite-loader/component lister-id {:on-viewport [:item-lister/request-items! lister-id]}])

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  [lister-id]
  ; XXX#1249 (source-code/cljs/engines/item_lister/body/effects.cljs)
  (cond @(r/subscribe [:item-lister/get-meta-item lister-id :engine-error])
         [error-element lister-id]
        @(r/subscribe [:item-lister/data-received? lister-id])
         [:<> [list-element    lister-id]
              ;[infinite-loader lister-id]
              [placeholder     lister-id]
              [ghost-element   lister-id]]
         :data-not-received
         [ghost-element lister-id]))

(defn body
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ; {:default-order-by (namespaced keyword)
  ;  :display-progress? (boolean)(opt)
  ;   Default: true
  ;  :clear-behaviour (keyword)(opt)
  ;   :none, :on-leave
  ;   Default: :none
  ;  :download-limit (integer)(opt)
  ;   Default: 20
  ;  :error-element (metamorphic-content)(opt)
  ;  :ghost-element (metamorphic-content)(opt)
  ;  :items-path (vector)(opt)
  ;   Default: core.helpers/default-items-path
  ;  :list-element (metamorphic-content)
  ;  :order-key (keyword)(opt)
  ;   Default: :order
  ;  :placeholder (metamorphic-content)(opt)
  ;   Default: :no-items-to-show
  ;  :prefilter (map)(opt)
  ;  :query (vector)(opt)
  ;  :transfer-id (keyword)(opt)}
  ;
  ; @usage
  ; [body :my-lister {...}]
  ;
  ; @usage
  ; (defn my-list-element [lister-id] [:div ...])
  ; [body :my-lister {:list-element #'my-list-element
  ;                   :prefilter    {:my-type/color "red"}}]
  [lister-id body-props]
  (let [body-props (body.prototypes/body-props-prototype lister-id body-props)]
       (reagent/lifecycles (core.helpers/component-id lister-id :body)
                           {:component-did-mount    (fn []  (r/dispatch [:item-lister/body-did-mount    lister-id body-props]))
                            :component-will-unmount (fn []  (r/dispatch [:item-lister/body-will-unmount lister-id]))
                            :component-did-update   (fn [%] (r/dispatch [:item-lister/body-did-update   lister-id %]))
                            :reagent-render         (fn []              [body-structure                 lister-id])})))
