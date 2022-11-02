
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.dictionary.books
    (:require [mid.x.dictionary.books.actions        :as books.actions]
              [mid.x.dictionary.books.application    :as books.application]
              [mid.x.dictionary.books.archive        :as books.archive]
              [mid.x.dictionary.books.brands         :as books.brands]
              [mid.x.dictionary.books.business       :as books.business]
              [mid.x.dictionary.books.changes        :as books.changes]
              [mid.x.dictionary.books.colors         :as books.colors]
              [mid.x.dictionary.books.company        :as books.company]
              [mid.x.dictionary.books.contacts       :as books.contacts]
              [mid.x.dictionary.books.content        :as books.content]
              [mid.x.dictionary.books.cookies        :as books.cookies]
              [mid.x.dictionary.books.database       :as books.database]
              [mid.x.dictionary.books.developer      :as books.developer]
              [mid.x.dictionary.books.edit           :as books.edit]
              [mid.x.dictionary.books.error-messages :as books.error-messages]
              [mid.x.dictionary.books.error-reports  :as books.error-reports]
              [mid.x.dictionary.books.errors         :as books.errors]
              [mid.x.dictionary.books.failures       :as books.failures]
              [mid.x.dictionary.books.favorites      :as books.favorites]
              [mid.x.dictionary.books.filters        :as books.filters]
              [mid.x.dictionary.books.inputs         :as books.inputs]
              [mid.x.dictionary.books.item           :as books.item]
              [mid.x.dictionary.books.languages      :as books.languages]
              [mid.x.dictionary.books.law            :as books.law]
              [mid.x.dictionary.books.layout         :as books.layout]
              [mid.x.dictionary.books.media          :as books.media]
              [mid.x.dictionary.books.money          :as books.money]
              [mid.x.dictionary.books.network        :as books.network]
              [mid.x.dictionary.books.notifications  :as books.notifications]
              [mid.x.dictionary.books.order-by       :as books.order-by]
              [mid.x.dictionary.books.people         :as books.people]
              [mid.x.dictionary.books.products       :as books.products]
              [mid.x.dictionary.books.search         :as books.search]
              [mid.x.dictionary.books.selection      :as books.selection]
              [mid.x.dictionary.books.seo            :as books.seo]
              [mid.x.dictionary.books.settings       :as books.settings]
              [mid.x.dictionary.books.share          :as books.share]
              [mid.x.dictionary.books.social-media   :as books.social-media]
              [mid.x.dictionary.books.sync           :as books.sync]
              [mid.x.dictionary.books.themes         :as books.themes]
              [mid.x.dictionary.books.temporary      :as books.temporary]
              [mid.x.dictionary.books.transfer       :as books.transfer]
              [mid.x.dictionary.books.units          :as books.units]
              [mid.x.dictionary.books.user           :as books.user]
              [mid.x.dictionary.books.vehicles       :as books.vehicles]
              [mid.x.dictionary.books.view           :as books.view]
              [mid.x.dictionary.books.website        :as books.website]
              [mid.x.dictionary.books.webshop        :as books.webshop]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOKS (merge books.actions/BOOK
                  books.application/BOOK
                  books.archive/BOOK
                  books.brands/BOOK
                  books.business/BOOK
                  books.changes/BOOK
                  books.colors/BOOK
                  books.company/BOOK
                  books.contacts/BOOK
                  books.content/BOOK
                  books.cookies/BOOK
                  books.database/BOOK
                  books.developer/BOOK
                  books.edit/BOOK
                  books.error-messages/BOOK
                  books.error-reports/BOOK
                  books.errors/BOOK
                  books.failures/BOOK
                  books.favorites/BOOK
                  books.filters/BOOK
                  books.inputs/BOOK
                  books.item/BOOK
                  books.languages/BOOK
                  books.law/BOOK
                  books.layout/BOOK
                  books.media/BOOK
                  books.money/BOOK
                  books.network/BOOK
                  books.notifications/BOOK
                  books.order-by/BOOK
                  books.people/BOOK
                  books.products/BOOK
                  books.search/BOOK
                  books.selection/BOOK
                  books.seo/BOOK
                  books.settings/BOOK
                  books.share/BOOK
                  books.social-media/BOOK
                  books.sync/BOOK
                  books.temporary/BOOK
                  books.themes/BOOK
                  books.transfer/BOOK
                  books.units/BOOK
                  books.user/BOOK
                  books.vehicles/BOOK
                  books.view/BOOK
                  books.website/BOOK
                  books.webshop/BOOK))
