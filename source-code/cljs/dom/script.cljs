
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.script
    (:require [dom.attribute :as attribute]
              [dom.body      :as body]
              [dom.node      :as node]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn append-script!
  ; @param (string) script
  ;
  ; @usage
  ;  (dom/append-script! "console.log('420')")
  [script]
  (let [body-element   (body/get-body-element)
        script-element (node/create-element! "script")]
       (attribute/set-element-attribute! script-element "type" "text/javascript")
       (node/set-element-content!        script-element script)
       (node/append-element!             body-element   script-element)))
