

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.body.views
    (:require [plugins.view-selector.body.prototypes :as body.prototypes]
              [plugins.view-selector.core.helpers    :as core.helpers]
              [reagent.api                           :as reagent]
              [x.app-core.api                        :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  [selector-id]
  (if-let [content @(a/subscribe [:view-selector/get-body-prop selector-id :content])]
          [content selector-id]))

(defn body
  ; @param (keyword) selector-id
  ; @param (map) body-props
  ;  {:content (metamorphic-content)
  ;   :default-view-id (keyword)}
  ;
  ; @usage
  ;  [view-selector/body :my-selector {...}]
  ;
  ; @usage
  ;  (defn my-content [selector-id] [:div ...])
  ;  [view-selector/body :my-selector {:content #'my-content}]
  [selector-id body-props]
  (let [];body-props (body.prototypes/body-props-prototype body-props)
       (reagent/lifecycles (core.helpers/component-id selector-id :body)
                           {:reagent-render         (fn []              [body-structure                   selector-id])
                            :component-did-mount    (fn []  (a/dispatch [:view-selector/body-did-mount    selector-id body-props]))
                            :component-will-unmount (fn []  (a/dispatch [:view-selector/body-will-unmount selector-id]))})))
                           ;:component-did-update   (fn [%] (a/dispatch [:view-selector/body-did-update   selector-id %]))
