
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.submit-button.views
    (:require [mid-fruits.map                          :refer [assoc-some]]
              [x.app-core.api                          :as a]
              [x.app-elements.button.prototypes        :as button.prototypes]
              [x.app-elements.button.views             :as button.views]
              [x.app-elements.submit-button.prototypes :as submit-button.prototypes]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; XXX#0714
  ; A submit-button elem alapkomponense a button elem.
  ; A submit-button elem további paraméterezését a button elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ;  {:input-ids (keywords in vector)(constant)(opt)
  ;    A továbblépéshez validálni és kitölteni szükséges mezők azonosítói
  ;   :label (metamorphic content)(opt)
  ;    Default: :submit!}
  ;
  ; @usage
  ;  [elements/submit-button {...}]
  ;
  ; @usage
  ;  [elements/submit-button :my-submit-button {...}]
  ([button-props]
   [element (a/id) button-props])

  ([button-id button-props]
   (let [button-disabled? @(a/subscribe [:elements.submit-button/button-disabled? button-id button-props])
         button-props (-> button-props submit-button.prototypes/button-props-prototype button.prototypes/button-props-prototype)
         button-props (assoc-some button-props :disabled? button-disabled?)]
        [button.views/element button-id button-props])))
