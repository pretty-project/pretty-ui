
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.core.lifecycles
    (:require [x.core.api :as x.core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:developer-tools.core/init!]})
