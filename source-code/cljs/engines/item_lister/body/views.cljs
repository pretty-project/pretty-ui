
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.body.views
    (:require [engines.item-lister.body.prototypes :as body.prototypes]
              [engines.item-lister.core.helpers    :as core.helpers]
              [re-frame.api                        :as r]
              [reagent.api                         :as reagent]
              [tools.infinite-loader.api           :as infinite-loader]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  ;   Default: 15
  ;  :items-path (vector)(opt)
  ;   Default: core.helpers/default-items-path
  ;  :order-key (keyword)(opt)
  ;   Default: :order
  ;  :prefilter (map)(opt)
  ;  :query (vector)(opt)
  ;  :transfer-id (keyword)(opt)}
  ;
  ; @usage
  ; [body :my-lister {...}]
  ;
  ; @usage
  ; [body :my-lister {:prefilter {:my-type/color "red"}}]
  [lister-id body-props]
  (let [body-props   (body.prototypes/body-props-prototype lister-id body-props)
        loader-props {:on-intersect [:item-lister/request-items! lister-id]}]
       (reagent/lifecycles (core.helpers/component-id lister-id :body)
                           {:component-did-mount    (fn []  (r/dispatch [:item-lister/body-did-mount    lister-id body-props]))
                            :component-will-unmount (fn []  (r/dispatch [:item-lister/body-will-unmount lister-id]))
                            :component-did-update   (fn [%] (r/dispatch [:item-lister/body-did-update   lister-id %]))
                            :reagent-render         (fn []              [infinite-loader/component lister-id loader-props])})))
