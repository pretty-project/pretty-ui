
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.sidebar-a.lifecycles
    (:require [x.server-core.api :as core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:environment/add-css! {:uri "/css/layouts/sidebar-a.css"}]})
