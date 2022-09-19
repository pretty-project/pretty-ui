
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.debug.resolvers
    (:require [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [mid-fruits.candy                      :refer [return]]
              [pathom.query.helpers                  :as query.helpers]
              [pathom.register.side-effects          :as register.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn debug-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [env _]
  (-> env query.helpers/env->query str println)
  (return "Follow the white rabbit"))

(defresolver debug
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @return (namespaced map)
             ;  {:pathom/debug (string)}
             [env resolver-props]
             {:pathom/debug (debug-f env resolver-props)})

(register.side-effects/reg-handler! :pathom/debug debug)
