
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.views
    (:require [app-fruits.reagent                    :as reagent]
              [plugins.view-selector.core.helpers    :as core.helpers]
              [plugins.view-selector.core.prototypes :as core.prototypes]
              [x.app-core.api                        :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  [extension-id]
  ; Ha a {:content ...} tulajdonságként átadott tartalom akkor jelenik meg, amikor elérhetővé
  ; vált a Re-Frame adatbázisban, akkor a rajta megjelenített tartalom nem iratkozik fel
  ; a view-selector plugin függvényeire, mielőtt a body komponens :component-did-mount életciklusa
  ; eltárolná a body-props paraméterként kapott tulajdonságait.
  (if-let [content @(a/subscribe [:view-selector/get-body-prop extension-id :content])]
          [content extension-id]))

(defn body
  ; @param (keyword) extension-id
  ; @param (map) body-props
  ;  {:allowed-view-ids (keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :default-view-id (keyword)(opt)
  ;    Default: core.config/DEFAULT-VIEW-ID}
  ;
  ; @usage
  ;  [view-selector/body :my-extension {...}]
  ;
  ; @usage
  ;  (defn my-content [extension-id] [:div ...])
  ;  [view-selector/body :my-extension {:content #'my-content}]
  [extension-id body-props]
  (let [body-props (core.prototypes/body-props-prototype body-props)]
       (reagent/lifecycles (core.helpers/component-id extension-id :body)
                           {:reagent-render      (fn []             [body-structure                extension-id])
                            :component-did-mount (fn [] (a/dispatch [:view-selector/body-did-mount extension-id body-props]))})))
