
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.views
    (:require [plugins.view-selector.core.helpers    :as core.helpers]
              [plugins.view-selector.core.prototypes :as core.prototypes]
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
  ;  {:allowed-view-ids (keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :default-view-id (keyword)(opt)
  ;    Default: core.config/DEFAULT-VIEW-ID}
  ;
  ; @usage
  ;  [view-selector/body :my-selector {...}]
  ;
  ; @usage
  ;  (defn my-content [selector-id] [:div ...])
  ;  [view-selector/body :my-selector {:content #'my-content}]
  [selector-id body-props]
  (let [body-props (core.prototypes/body-props-prototype body-props)]
       (reagent/lifecycles (core.helpers/component-id selector-id :body)
                           {:reagent-render         (fn []             [body-structure                   selector-id])
                            :component-did-mount    (fn [] (a/dispatch [:view-selector/body-did-mount    selector-id body-props]))
                            :component-will-unmount (fn [] (a/dispatch [:view-selector/body-will-unmount selector-id]))})))
