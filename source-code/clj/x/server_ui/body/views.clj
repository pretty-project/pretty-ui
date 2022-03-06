
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.body.views
    (:require [mid-fruits.candy             :refer [param return]]
              [mid-fruits.vector            :as vector]
              [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
              [x.server-core.api            :as a]
              [x.server-router.api          :as router]
              [x.server-ui.body.engine      :as body.engine]
              [x.server-ui.engine           :refer [include-js]]
              [x.server-ui.graphics.views   :as graphics.views]
              [x.server-ui.shield.views     :refer [view] :rename {view app-shield}]
              [x.server-user.api            :as user]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) body-props
  ;  {:plugin-js-paths (maps in vector)(opt)}
  ;
  ; @return (map)
  ;  {:app-build (string)
  ;   :core-js (string)
  ;   :selected-theme (string)
  ;   :shield (hiccup)}
  [request body-props]
  (let [app-config @(a/subscribe [:core/get-app-config])]
       (merge app-config body-props
              {:app-build      (a/app-build)
               :core-js        (router/request->core-js          request)
               :selected-theme (user/request->user-settings-item request :selected-theme)
               :shield         (app-shield (graphics.views/loading-animation))
               ; Hozzáadja a {:plugin-js-paths [...]} paraméterként átadott útvonalakat
               ; az x.app-config.edn fájlban beállított útvonalakhoz
               :plugin-js-paths (vector/concat-items (:plugin-js-paths app-config)
                                                     (:plugin-js-paths body-props))})))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) body-props
  ;  {:selected-theme (string)
  ;   :shield (hiccup)(opt)}
  [request {:keys [selected-theme shield]}]
  [:body#x-body-container {:data-theme selected-theme}
                          (let [csrf-token (force *anti-forgery-token*)]
                               [:div#sente-csrf-token {:data-csrf-token csrf-token}])
                          [:div#x-app-container]
                          (param shield)])

(defn view
  ; @param (map) request
  ; @param (map)(opt) body-props
  ;  {:plugin-js-paths (maps in vector)
  ;    [{:core-js (string)(opt)
  ;      :uri (string)}]
  ;   :shield (hiccup)(opt)}
  ;
  ; @usage
  ;  (ui/body {...} {:shield [:div#x-app-shield "My loading screen"]})
  ([request]
   (view request {}))

  ([request body-props]
   (let [body-props (body-props-prototype request body-props)]
        (-> (body                          request body-props)
            (body.engine/body<-js-includes request body-props)))))
