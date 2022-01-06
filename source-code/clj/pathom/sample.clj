
(ns pathom.sample
    (:require [pathom.api :as pathom]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; -- Debug -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A :debug resolver függvény teljes query vektort kiírja a szerveri-oldali konzolra!
;
; Pl.: [:debug `(:get-my-resolver ~{...})]



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-my-resolver-f
  [env resolver-props]
  (let [my-request (pathom/env->request env)
        my-params  (pathom/env->params  env)
        my-param   (pathom/env->param   env :my-param)]
       (str "My return value")))

(defresolver get-my-resolver
             [env resolver-props]
             {:get-my-resolver (get-my-resolver-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn do-your-mutation-f
  [env mutation-props]
  (str "Your return value"))

(defmutation do-your-mutation!
             [env mutation-props]
             {::pco/op-name 'do-your-mutation!}
             (do-your-mutation-f env mutation-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(pathom/reg-handler!  ::handler  [get-my-resolver])
(pathom/reg-handlers! ::handlers [do-your-mutation!])
