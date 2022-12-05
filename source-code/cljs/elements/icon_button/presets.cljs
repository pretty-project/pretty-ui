
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.icon-button.presets)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BUTTON-PROPS-PRESETS
     {; Defaults:
      :default            {}
      :muted              {:color       :muted}
      :primary            {:color       :primary}
      :secondary          {:color       :secondary}
      :success            {:color       :success}
      :warning            {:color       :warning}
      ; *****
      :add                {:color       :primary
                           :icon        :add_circle}
      :add-to-favorites   {:color       :secondary
                           :icon        :favorite_border}
      :added-to-favorites {:color       :secondary
                           :icon        :favorite}
      :apps               {:icon        :apps}
      :archive            {:icon        :inventory_2
                           :icon-family :material-icons-outlined}
      :archived           {:icon        :inventory_2}
      :back               {:icon        :arrow_back}
      :cancel             {:icon        :close}
      :checked            {:icon        :check_box}
      :unchecked          {:icon        :check_box_outline_blank}
      :close              {:icon        :close}
      :delete             {:color       :warning
                           :icon        :delete_outline}
      :duplicate          {:icon        :content_copy}
      :edit               {:color       :primary
                           :icon        :edit}
      :filters-visible    {:icon        :filter_alt}
      :filters-nonvisible {:icon        :filter_alt
                           :icon-family :material-icons-outlined}
      :forward            {:icon        :arrow_forward}
      :fullscreen         {:icon        :fullscreen}
      :home               {:icon        :home}
      :menu               {:icon        :more_vert}
      :menu-bar           {:icon        :more_horiz}
      :next               {:icon        :arrow_forward_ios}
      :prev               {:icon        :arrow_back_ios}
      :reorder            {:icon        :reorder}
      :restore            {:icon        :restart_alt}
      :revert             {:icon        :xxx}
      :save               {:color       :primary
                           :icon        :save}
      :search             {:icon        :search}
      :select             {:icon        :check_box_outline_blank}
      :order-by           {:icon        :sort}
      :user-menu          {:icon        :account_circle}})
